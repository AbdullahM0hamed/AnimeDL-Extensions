
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.errors.MessageReceiverImpl
import com.android.build.gradle.options.SyncOptions.ErrorFormatMode
import com.android.builder.dexing.ClassFileInputs
import com.android.builder.dexing.DexArchiveBuilder
import com.android.builder.dexing.DexParameters
import com.android.builder.dexing.r8.ClassFileProviderFactory
import com.google.common.io.Closer
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.slf4j.LoggerFactory
import java.io.File
import java.util.stream.Collectors

object CompileDex {
    fun compile(project: Project) {
        val input: ConfigurableFileCollection = project.objects.fileCollection()
        val android = project.extensions.getByName("android") as BaseExtension
        val dexOutputDir = project.buildDir.resolve("outputs")

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
