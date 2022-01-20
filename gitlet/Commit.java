package gitlet;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

/** commit class.
 * @author Mark Yang
 */
public class Commit implements Serializable {
    /** run function REPO ARGS. */
    public void run(File repo, String[] args) {
        if (args[0].equals("initial commit")) {
            if (!format(args, 1, true)) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
        } else {
            if (!format(args, 2, true)) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
            if (args[1].equals("")) {
                System.out.println("Please enter a commit message.");
                return;
            }
        }
        Stage stage = Utils.readObject(
                Utils.join(repo, "stage", "staging.ser"), Stage.class);

        if (stage.getHead() != null
                && stage.getRemoved().isEmpty() && stage.isstageempty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        for (String filename : stage.getStaged().keySet()) {
            String blobid = stage.getStaged().get(filename);
            stage.track(filename, blobid);
        }
        stage.emptystage();
        String timestamp =
                new SimpleDateFormat("EEE MMM dd "
                        + "hh:mm:ss YYYY Z").format(new Date());
        String cmessage = null;
        if (args.length == 2) {
            cmessage = args[1];
        } else if (args.length == 1) {
            cmessage = args[0];
        }
        String parentid = stage.getHead();
        String branch = stage.getBranch();
        String shaid = Utils.sha1(timestamp + cmessage);

        CommitObject curr = new CommitObject(shaid,
                cmessage, timestamp, parentid, null, branch, stage);
        stage.addbranch(branch, curr.getSHA());
        stage.addcommit(curr);
        File currcommitfile = Utils.join(repo, "commit",
                curr.getSHA() + ".ser");
        stage.setHead(curr.getSHA());
        Utils.writeObject(currcommitfile, curr);
        Utils.writeObject(Utils.join(repo, "stage/staging.ser"), stage);

    }
    /** Format check with ARGS, NUMB, REXIST @return boolean. */
    public boolean format(String[] args, int numb, boolean rexist) {
        if (rexist) {
            if (!new File(".gitlet/").exists()) {
                System.out.println("Not in an initialized Gitlet directory.");
                return false;
            }
        }
        if (args.length != numb) {
            System.out.println("Incorrect operands.");
            return false;
        }
        return true;
    }
}
