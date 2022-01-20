package gitlet;
import java.io.Serializable;
import java.util.HashMap;

/** stage class.
 * @author Mark Yang
 */
public class Stage implements Serializable {
    /** create stage. */
    public Stage() {
        commits = new HashMap<>();
        tracked = new HashMap<>();
        staged = new HashMap<>();
        removed = new HashMap<>();
        branches = new HashMap<>();
    }
    /** add it to stage hashmap with FILENAME, SHA1. */
    public void stage(String filename, String sha1) {
        staged.put(filename, sha1);
    }
    /** unstage the FILENAME with SHAID. */
    public void unstage(String filename, String shaid) {
        if (staged.containsKey(filename)) {
            staged.remove(filename);
        }
    }
    /** tracks the FILENAME and SHAID. */
    public void track(String filename, String shaid) {
        tracked.put(filename, shaid);
    }
    /** untracks the FILENAME and SHAID. */
    public void untrack(String filename, String shaid) {
        if (tracked.containsKey(filename)) {
            tracked.remove(filename);
        }
    }
    /** removes the FILENAME and SHAID. */
    public void remove(String filename, String shaid) {
        removed.put(filename, shaid);
        untrack(filename, shaid);
        unstage(filename, shaid);
    }
    /** unremoves the FILENAME and SHAID. */
    public void unremove(String filename, String shaid) {
        if (removed.containsKey(filename)) {
            removed.remove(filename);
        }
    }
    /** adds a branch with a BRANCHNAME and SHAID. */
    public void addbranch(String branchname, String shaid) {
        branches.put(branchname, shaid);
    }
    /** gets the commit @return hasmap. */
    public HashMap<String, CommitObject> getcommit() {
        return commits;
    }
    /** adds a COMMITING to a hashmap. */
    public void addcommit(CommitObject commiting) {
        commits.put(commiting.getSHA(), commiting);
    }
    /** gets the tracked file @return hasmap. */
    public HashMap<String, String> gettracked() {
        return tracked;
    }
    /** boolean that checks if the FILENAME is tracked @return boolean. */
    public Boolean istracked(String filename) {
        return tracked.containsKey(filename);
    }
    /** gets the staged files @return hasmap. */
    public HashMap<String, String> getStaged() {
        return staged;
    }
    /** boolean that checks if FILENAME is staged @return boolean. */
    public Boolean isstaged(String filename) {
        return staged.containsKey(filename);
    }
    /** boolean that checks if the stage is empty @return boolean. */
    public Boolean isstageempty() {
        return staged.isEmpty();
    }
    /** get the removed files @return hashmap. */
    public HashMap<String, String> getRemoved() {
        return removed;
    }
    /** function that empties the stage. */
    public void emptystage() {
        staged = new HashMap<>();
        removed = new HashMap<>();
    }
    /** gets the hashmap branches @return hashmap. */
    public HashMap<String, String> getBranches() {
        return branches;
    }
    /** gets the head @return string. */
    public String getHead() {
        return head;
    }
    /** sets the COMMITSHA as head. */
    public void setHead(String commitsha) {
        head = commitsha;
    }
    /** gets the branches @return String. */
    public String getBranch() {
        return branch;
    }
    /** set the BRANCHNAME. */
    public void setBranches(String branchname) {
        branch = branchname;
    }




    /** Hashmap of commits. */
    private HashMap<String, CommitObject> commits;
    /** hashmap of tracked files. */
    private HashMap<String, String> tracked;
    /** hashmap of staged files. */
    private HashMap<String, String> staged;
    /** hashmap of removed files. */
    private HashMap<String, String> removed;
    /** hashmap of branches. */
    private HashMap<String, String> branches;
    /** Head variable. */
    private String head;
    /** branch variable. */
    private String branch;

}
