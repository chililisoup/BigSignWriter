plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.10-SNAPSHOT" apply false
}
stonecutter active "1.21.2"

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter parameters {
    /*
    See src/main/java/com/example/TemplateMod.java
    and https://stonecutter.kikugie.dev/
    */
}