# maestro-scm-plugin
Maestro plugin providing a "task" to wrapping the Maven SCM Library. This
plugin is a Java-based deployable that gets delivered as a Zip file.

<http://maven.apache.org/scm/>

Manifest:

* manifest.json
* README.md (this file)

## The Perforce Task
This SCM plugin requires a few inputs:

* **host** (location of the repository)
* **port** (port the server is listening to)
* **path** (filesystem location of the working copy)
* **command** (maven scm command, eg checkout, update, status)
* **username** (username for connecting to remote repositories)
* **password** (password for connecting to remote repositories)
* **depot** (depot name from repository)
* **project_path** (path to project inside the depot)
* **version_type** (tag, branch, revision)
* **version** (identifier for the version type given)
* **auto_login** (task will attempt to login and create a ticket for later use)

## License
Apache 2.0 License: <http://www.apache.org/licenses/LICENSE-2.0.html>
