include(":stubs")
include(":core")

// Load all extensions
File(rootDir, "src").eachDir { dir ->
    dir.eachDir { subdir ->
        val name = ":${dir.name}-${subdir.name}"
        include(name)
        project(name).projectDir = File("src/${dir.name}/${subdir.name}")
    }
}

inline fun File.eachDir(block: (File) -> Unit) {
    listFiles()?.filter { it.isDirectory }?.forEach { block(it) }
}
