package info.victorchu.j8.usage.proxy;

public class RealSubject implements Subject{
    @Override
    public void logIn(){
        System.out.println("Logging In ...");
    }

    @Override
    public void playGames(){
        System.out.println("Playing Games ...");
    }

    @Override
    public void logOut(){
        System.out.println("Logging Out ...");
    }
}
