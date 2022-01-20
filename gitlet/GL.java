package gitlet;
import java.io.File;

/** global log class.
 * @author markyang
 */
public class GL {
    /** Exec function with REPO and ARGS. */
    public void exec(File repo, String[] args) {
        if (!check(args, 1, true)) {
            return;
        }
        for (String commit : Utils.join(repo, "commit").list()) {
            File curr = Utils.join(repo, "commit/" + commit);
            CommitObject currcommit =
                    Utils.readObject(curr, CommitObject.class);
            Utils.message("===");
            Utils.message("\ncommit " + currcommit.getSHA());
            if (currcommit.getParent2SHA() != null) {
                Utils.message("\nMerge: "
                        + currcommit.getParentSHA().substring(0, 7)
                        + currcommit.getParent2SHA().substring(0, 7));
            }
            Utils.message("\nDate: " + currcommit.getTimeStamp());
            Utils.message("\n" + currcommit.getLogMessage());
        }
    }
    /** style check with ARGS, ARGSNUMBER, NEEDSREPO @return Boolean. */
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
