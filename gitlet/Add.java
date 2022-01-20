package gitlet;

import java.io.File;
import java.io.Serializable;

/** add class.
 *  @author Mark Yang.
 */
public class Add implements Serializable {
    /** saves REPO and ARGS. */
    public void run(File repo, String[] args) {
        if (args.length != 2) {
            if (args.length < 2) {
                System.out.println("Please enter a commit message.");
                return;
            } else {
                System.out.println("Incorret number of operands.");
                return;
            }
        }
        boolean modified = true;
        String fileName = args[1];
        File workingdirectory = new File(System.getProperty("user.dir"));
        File toAdd = new File(workingdirectory, fileName);
        if (!toAdd.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        File file = Utils.join(repo, "stage/staging.ser");
        Stage stage = Utils.readObject(file, Stage.class);
        String newFileSHA = Utils.sha1(Utils.readContents(toAdd));
        String headCommitSHA = stage.getHead();

        if (headCommitSHA != null) {
            File commitfile = Utils.join(repo, "commit",
                    headCommitSHA + ".ser");
            CommitObject headCommit =
                    Utils.readObject(commitfile, CommitObject.class);
            if (headCommit != null
                    && headCommit.getTrackedBlobs().containsKey(fileName)) {
                String oldFileSHA = headCommit.getTrackedBlobs().get(fileName);
                modified = !oldFileSHA.equals(newFileSHA);
            }
        }

        if (stage.getRemoved().containsKey(fileName)) {
            stage.unremove(fileName, newFileSHA);
        } else if (modified) {
            stage.stage(fileName, newFileSHA);
            Blob blob = new Blob(newFileSHA, fileName,
                    Utils.readContents(toAdd));
            byte[] sblob = Utils.serialize(blob);
            File newfile = Utils.join(repo, "blob", newFileSHA + ".ser");
            Utils.writeContents(newfile, sblob);
        }
        byte[] serial = Utils.serialize(stage);
        Utils.writeContents(Utils.join(repo, "stage/staging.ser"), serial);
    }
}
