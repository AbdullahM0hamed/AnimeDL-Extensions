
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.errors.MessageReceiverImpl
import com.android.build.gradle.options.SyncOptions.ErrorFormatMode
import com.android.builder.dexing.ClassFileInputs
import com.android.builder.dexing.DexArchiveBuilder
import com.android.builder.dexing.DexParameters
import com.android.builder.dexing.r8.ClassFileProviderFactory
import com.google.common.io.Closer
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionContainer
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import java.util.Arrays
import java.util.stream.Collectors

object CompileDex {
    fun compile(
        extensions: ExtensionContainer,
        buildDir: File,
        objects: ObjectFactory
    ) {
        val input: ConfigurableFileCollecroject.objects.fileCollection()
        val android = extensions.getByName("android") as BaseExtension
        val dexOutputDir = buildDir.resolve("outputs")

        Closer.create().use { closer ->
            val dexBuilder = DexArchiveBuilder.createD8DexBuilder(
                DexParameters(
                    minSdkVersion = android.defaultConfig.maxSdkVersion ?: 24,
                    debuggable = true,
                    dexPerClass = false,
                    withDesugaring = true,
                    desugarBootclasspath = ClassFileProviderFactory(android.bootClasspath.map(File::toPath))
                        .also { closer.register(it) },
                    desugarClasspath = ClassFileProviderFactory(listOf<Path>()).also { closer.register(it) },
                    coreLibDesugarConfig = null,
                    coreLibDesugarOutputKeepRuleFile = null,
                    messageReceiver = MessageReceiverImpl(
                        ErrorFormatMode.HUMAN_READABLE,
                        LoggerFactory.getLogger(CompileDex::class.java)
                    )
                )
            )

            val fileStreams =
                input.map { input -> ClassFileInputs.fromPath(input.toPath()).use { it.entries { _, _ -> true } } }
                    .toTypedArray()

            Arrays.stream(fileStreams).flatMap { it }
                .use { classesInput ->
                    val files = classesInput.collect(Collectors.toList())

                    dexBuilder.convert(
                        files.stream(),
                        dexOutputDir.toPath()
                    )
                }
        }
    }
}
