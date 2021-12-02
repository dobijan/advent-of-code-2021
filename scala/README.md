# Scala

This folder contains the solution written in Scala. It utilizes Cats Effect's `IO` monad
for managing file resources in which the puzzles are defined. The computations are
thus performed within `IO`.

This is an `sbt` project. Java 8+ needs to be installed. The project contains `sbtx`, a
wrapper for `sbt` which can be uses to run it without an `sbt` installation.

## Usage

Each day has a separate main class. It can either be run from an IDE or via `sbt`. 

To run it from the command line:

```shell
./sbtx "Test / runMain jasek.aoc2021.Day1SonarSweepTest"
[info] welcome to sbt 1.5.5 (Azul Systems, Inc. Java 17.0.1)
[info] loading project definition from /home/jasek/Documents/code/advent-of-code-2021/scala/project
[info] loading settings for project aoc2021 from build.sbt ...
[info] set current project to advent-of-code-2021 (in build file:/home/jasek/Documents/code/advent-of-code-2021/scala/)
[warn] sbt 0.13 shell syntax is deprecated; use slash syntax instead: Test / runMain
[info] running jasek.aoc2021.Day1SonarSweepTest 
2021.12.02 14:48:31:000 [io-comp...] [INFO ] jasek.aoc2021.Day1SonarSweepTest.run:19 - 
With a 1-wide measurement window depth has increased 1665 times.
With a 3-wide measurement window depth has increased 1702 times.

[success] Total time: 1 s, completed 2 Dec 2021, 14:48:31
```

Or, if multiple days are to be executed, to speed things up one can start `sbt` once
and issue commands to the same process:

```shell
./sbtx 
[info] welcome to sbt 1.5.5 (Azul Systems, Inc. Java 17.0.1)
[info] loading project definition from /home/jasek/Documents/code/advent-of-code-2021/scala/project
[info] loading settings for project aoc2021 from build.sbt ...
[info] set current project to advent-of-code-2021 (in build file:/home/jasek/Documents/code/advent-of-code-2021/scala/)
[info] sbt server started at local:///home/jasek/.sbt/1.0/server/2f7c4358b07e4ab8593b/sock
[info] started sbt server
sbt:advent-of-code-2021> 
```

```shell
sbt:advent-of-code-2021> Test / runMain jasek.aoc2021.Day1SonarSweepTest
[info] running jasek.aoc2021.Day1SonarSweepTest 
2021.12.02 14:53:59:000 [io-comp...] [INFO ] jasek.aoc2021.Day1SonarSweepTest.run:19 - 
With a 1-wide measurement window depth has increased 1665 times.
With a 3-wide measurement window depth has increased 1702 times.

[success] Total time: 0 s, completed 2 Dec 2021, 14:53:59
sbt:advent-of-code-2021> 
```