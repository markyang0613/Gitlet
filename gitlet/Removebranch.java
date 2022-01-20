package gitlet;
import java.io.File;
import java.io.Serializable;

/** removebranch class.
 * @author markyang
 */
public class Removebranch implements Serializable {
    /** execfunction with REPO and ARGS. */
    public void exec(File repo, String[] args) {
        if (!check(args, 2, true)) {
            System.out.println("Incorrect operands");
            return;
        }
        Stage stage = Utils.readObject(
                Utils.join(repo, "stage/staging.ser"), Stage.class);
        String cb = stage.getBranch();
        String toberemoved = args[1];

        if (stage.getBranches().containsKey(toberemoved)) {
            if (!toberemoved.equals(cb)) {
                stage.getBranches().remove(toberemoved);
                Utils.writeObject(Utils.join(repo, "stage/staging.ser"), stage);
            } else {
                System.out.println("Cannot remove the current branch.");
                return;
            }
        } else {
            System.out.println("A branch with that name does not exist.");
            return;
        }
    }
    /** style checking ARGS, ARGSNUMBER, and NEEDSREPO @return boolean. */
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
