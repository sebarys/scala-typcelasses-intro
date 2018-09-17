# scala-typelevel

This project is using `sbt-groll` ( https://github.com/sbt/sbt-groll ) - plugin that allow for easy transition between consecutive commits.

If you want:
- go to init commit: execute `sbt groll init`
- go next commit: execute `sbt groll next`
- go previous commit: execute `sbt groll prev`
- get current commitId and message: execute `sbt groll show`
- get the full commit history: execute `sbt groll list`