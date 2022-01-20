package gitlet;
import java.io.File;

import java.io.Serializable;

/** status class.
 * @author markyang
 */
public class Status implements Serializable {
    /** execfunction with REPO and ARGS. */
    public void exec(File repo, String[] args) {
        if (!check(args, 1, true)) {
            return;
        }
        Utils.message("=== Branches ===");
        Stage stage = Utils.readObject(
                Utils.join(repo, "stage/staging.ser"), Stage.class);
        Utils.message("*" + stage.getBranch());
        for (String bn : stage.getBranches().keySet()) {
            if (!bn.equals(stage.getBranch())) {
                Utils.message(bn);
            }
        }

        Utils.message("\n=== Staged Files ===");
        for (String fn : stage.getStaged().keySet()) {
            Utils.message(fn);
        }
        Utils.message("\n=== Removed Files ===");
        for (String fn : stage.getRemoved().keySet()) {
            Utils.message(fn);
        }

        Utils.message("\n=== Modifications Not Staged For Commit ===");
        Utils.message("\n=== Untracked Files ===\n");

    }
    /** checking the format with ARGS,
     * ARGSNUMBER, and NEEDSREPO @return boolean. */
    public boolean check(String[] args, int argsNumber, boolean needsRepo) {
        if (needsRepo) {
            if (!new File(".gitlet/").exists()) {
                System.out.println("Not in an initialized Gitlet directory.");
                return false;
            }
        }
        if (args.length != argsNumber) {
            System.out.println("Incorrect operands.");
            return false;
        }
        return true;
    }
}
