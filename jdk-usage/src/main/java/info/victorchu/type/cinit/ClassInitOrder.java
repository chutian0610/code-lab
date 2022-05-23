package info.victorchu.type.cinit;

public class ClassInitOrder {

    public static void main(String[] args) {
        Parent parent = new Child();
    }
}

class Sample {

    String s;

    Sample(String s) {
        this.s = s;
        System.out.println(s);
    }
    Sample(String s,String old) {
        this.s = s;
        System.out.println(s+"->"+old);
    }

    static Sample init(Sample s,String str) {
        if(s == null){
            return new Sample(str);
        }else {
            return new Sample(str,s.toString());
        }
    }

    @Override
    public String toString() {
        return this.s;
    }
}


