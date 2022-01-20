package gitlet;
import java.io.File;
import java.io.Serializable;

/** remove class.
 * @author Mark Yang
 */
public class Remove implements Serializable {
    /** exec function with REPO and ARGS. */
    public void exec(File repo, String[] args) {
        if (!check(args, 2, true)) {
            return;
        }
        Stage stage = Utils.readObject(
                Utils.join(repo, "stage/staging.ser"), Stage.class);
        String removingfilnename = args[1];
        if (!stage.istracked(removingfilnename)
                && !stage.isstaged(removingfilnename)) {
            System.out.println("No reason to remove the file.");
        }
        if (stage.istracked(removingfilnename)) {
            String id = stage.gettracked().get(removingfilnename);
            stage.remove(removingfilnename, id);
            stage.untrack(removingfilnename, id);
        } else if (stage.isstaged(removingfilnename)) {
            String id = stage.getStaged().get(removingfilnename);
            stage.unstage(removingfilnename, id);
        }
        Utils.writeObject(
                Utils.join(repo, "stage/staging.ser"), stage);
        File rfile = Utils.join(
                System.getProperty("user.dir"), removingfilnename);
        if (rfile.exists()) {
            rfile.delete();
        }
    }
    /** style checking ARGS, ARGSNUMBER and NEEDSREPO @return boolean. */
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
