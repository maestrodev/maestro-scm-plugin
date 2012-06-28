# maestro-scm-plugin
Maestro plugin providing a "task" to wrapping the Maven SCM Library. This
plugin is a Java-based deployable that gets delivered as a Zip file.

<http://maven.apache.org/scm/>

Manifest:

* manifest.json
* README.md (this file)

## The Perforce Task
The Perforce task supports several commands which can be entered in the command field, they are:

branch - branch the project
validate - validate the scm information in the pom
* **add** - command to add file
* **unedit** - command to stop editing the working copy
* **export** - command to get a fresh exported copy
* **bootstrap** - command to checkout and build a project
* **changelog** - command to show the source code revisions
* **list** - command for get the list of project files
* **checkin** - command for commiting changes
* **checkout** - command for getting the source code
* **status** - command for showing the scm status of the working copy
* **update** - command for updating the working copy with the latest changes
* **diff**- command for showing the difference of the working copy with the remote one
* **update** -subprojects - command for updating all projects in a multi project build
* **edit** - command for starting edit on the working copy
* **tag** - command for tagging a certain revision

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
