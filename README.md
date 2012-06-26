# maestro-scm-plugin
Maestro plugin providing a "task" to wrapping the Maven SCM Library. This
plugin is a Java-based deployable that gets delivered as a Zip file.

<http://maven.apache.org/scm/>

Manifest:

* manifest.json
* README.md (this file)

## The Task
This SCM plugin requires a few inputs:

* **url** (location of the repository)
* **path** (filesystem location of the working copy)
* **command** (maven scm command, eg checkout, update, status)
* **username** (username for connecting to remote repositories)
* **password** (password for connecting to remote repositories)
* **private_key** (private_key location for connecting to remote repositories)
* **passphrase** (passphrase for connecting to remote repositories)


## License
Apache 2.0 License: <http://www.apache.org/licenses/LICENSE-2.0.html>
