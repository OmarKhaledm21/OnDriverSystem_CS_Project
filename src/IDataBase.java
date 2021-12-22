public interface IDataBase {
    public boolean addUser(User user);
    public boolean activateUser(User user);
    public boolean deleteUser(User user);
    public boolean suspendUser(User user);
    public User search(String username);
    public boolean userExist(User user);
}
