# maestro-scm-plugin
Maestro plugin providing a "task" to wrapping the Maven SCM Library. This
plugin is a Java-based deployable that gets delivered as a Zip file.

<http://maven.apache.org/scm/>

Manifest:

* manifest.json
* README.md (this file)

## The Perforce Sync Task
The Perforce Sync Task
brings the client workspace into sync with the depot by copying files matching its file pattern arguments from the depot to the client workspace. When no file patterns are specified on the command line, p4 sync copies a particular depot file only if it meets all of the following criteria:
* The file must be visible through the client view;
* It must not already be opened by p4 edit, p4 delete, p4 add, or p4 integrate;
* It must not already exist in the client workspace at its latest revision (the head revision).
		In new, empty, workspaces, all depot files meet the last two criteria, so all the files visible through the workspace view are copied into the user's workspace.
		If file patterns are specified on the command line, only those files that match the file patterns and that meet the above criteria are copied.
		If the file pattern contains a revision specifier, the specified revision is copied into the client workspace.
		If the file argument includes a revision range, only files selected by the revision range are updated, and the highest revision in the range is used. Files that are no longer in the workspace view are not affected if the file argument includes a revision range.
		The newly synced files are not available for editing until opened with p4 edit or p4 delete. Newly synced files are read-only; p4 edit and p4 delete make the files writable. Under normal circumstances, do not use your operating system's commands to make the files writable; instead, use Perforce to do this for you.

The Perforce Sync Task requires a few inputs:

* **host** (location of the repository)
* **port** (port the server is listening to)
* **path** (filesystem location of the working copy)
* **client_name** (name of the client to be specified)
* **view_mappings** (list of client spec view mappings, how depot files will be 'mapped' into your workspace)
* **username** (username for connecting to remote repositories)
* **password** (password for connecting to remote repositories)
* **force_sync** (Perforce performs the sync even if the client workspace already has the file at the specified revision. If the file is writable, it is overwritten.
					This flag does not affect open files, but it does override the noclobber client option)
* **no_update** (causes sync not to update the client workspace, but to display what normally would be updated. Corresponds to the p4 sync "-n" flag)
* **bypass_client** (bypasses the client file update. It can be used to make the server believe that a client workspace already has the file. Corresponds to the p4 sync "-k" flag)
* **bypass_server** (populates the client workspace, but does not update the server to reflect those updates. Any file that is already synced or opened will be bypassed with a warning message. Corresponds to the p4 sync "-p" flag)
* **fail_on_up_to_date** (causes the task to fail if the given workspace is up to date)
* **clean_working_copy** (removes the workspace root before performing the sync task)

## License
Apache 2.0 License: <http://www.apache.org/licenses/LICENSE-2.0.html>
