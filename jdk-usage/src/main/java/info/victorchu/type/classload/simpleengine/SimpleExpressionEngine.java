package info.victorchu.type.classload.simpleengine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static info.victorchu.type.classload.simpleengine.ScriptFactory.isStringEmpty;

/**
 * facade
 *
 */
public class SimpleExpressionEngine {

    private Map<String, Class>  scriptCache = new ConcurrentHashMap<String, Class>();

    private static String DefaultScriptPrefixName = "SimpleExpression_";
    private String scriptPrefixName;
    private JavaClassHelper javaClassHelper = new JavaClassHelper();


    public SimpleExpressionEngine(String scriptPrefixName){
        this.scriptPrefixName = isStringEmpty(scriptPrefixName)? DefaultScriptPrefixName:scriptPrefixName.trim();
    }

    public String getScriptPrefixName() {
        return scriptPrefixName;
    }

    public Object evalute(String exp,Map<String,Object> env) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        Script script = new Script();
        script.setOriginExp(exp);
        script = javaClassHelper.loadScript(script,scriptPrefixName,env);
        return script.getExpression().run(env);

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        SimpleExpressionEngine simpleExpressionEngine = new SimpleExpressionEngine(null);
        String  exp = "return 1000+100.0*99-(600-3*15)/(((68-9)-3)*2-100)+10000%7*71;";
        Map<String,Object> env = new HashMap<>();
        for (int i = 0; i <10 ; i++) {
            System.out.println(simpleExpressionEngine.evalute(exp,env));
        }
    }

}