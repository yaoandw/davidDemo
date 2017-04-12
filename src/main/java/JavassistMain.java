/**
 * Created by yaoandw on 2017/4/12.
 *
 * localhost:davidDeomo yaoandw$ jar uvf src/main/resources/charles.jar com/xk72/charles/License.class
 * 正在添加: com/xk72/charles/License.class(输入 = 13691) (输出 = 5512)(压缩了 59%)
 */

import javassist.*;

public class JavassistMain {
    public static void main(String[] args) throws Exception {
        ClassPool classPool=ClassPool.getDefault();
        classPool.insertClassPath("/Users/yaoandw/Documents/java/intellijWorkspace/davidDeomo/src/main/resources/original-charles.jar"); //复制出来charles.jar的文件路径

        CtClass ctClass = classPool.get("com.xk72.charles.License");
        CtMethod ctMethod =ctClass.getDeclaredMethod("a",null);
        ctMethod.setBody("{return true;}");
        ctMethod = ctClass.getDeclaredMethod("b",null);
        ctMethod.setBody("{return \"yycon\";}");
        ctClass.writeFile();

    }
}
