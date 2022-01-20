package gitlet;
import java.io.File;
import java.util.HashMap;

/** reset class.
 * @author markyang
 */
public class Reset extends Removebranch {
    /** funcyion exec with REPO and ARGS. */
    public void exec(File repo, String[] args) {
        if (!check(args, 2, true)) {
            System.out.println("Incorrect operands.");
            return;
        }
        String commitid = args[1];
        Stage stage = Utils.readObject(
                Utils.join(repo, "stage/staging.ser"), Stage.class);
        if (!stage.getcommit().keySet().contains(commitid)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        for (String shaid : stage.getcommit().keySet()) {
            if (shaid.equals(commitid)) {
                CommitObject commit = Utils.readObject(
                        Utils.join(repo, "commit"
                                + commitid + ".ser"), CommitObject.class);
                HashMap<String, String> blob = commit.getTrackedBlobs();
                String filename = null;
                String fileid = null;
                for (File file : repo.listFiles()) {
                    filename = file.getName();
                    if (!blob.containsKey(filename)
                            && stage.gettracked().containsKey(filename)) {
                        fileid = stage.gettracked().get(filename);
                        stage.untrack(filename, fileid);
                        Utils.restrictedDelete(file);
                    }
                }
                for (String blobname : blob.keySet()) {
                    String blobid = blob.get(blobname);
                    Blob blobfile = Utils.readObject(
                            Utils.join(repo, "blob" + blobid
                                    + ".ser"), Blob.class);
                    byte[] content = blobfile.getContents();
                    Utils.writeContents(new File(blobname), content);
                    stage.track(blobname, blobfile.getSHA1());
                }
                stage.emptystage();
                stage.setHead(commit.getSHA());
                stage.addbranch(commit.getBranch(), commit.getSHA());
                Utils.writeObject(Utils.join(repo, "stage"), stage);
            }
        }
    }
}
