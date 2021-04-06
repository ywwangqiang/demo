#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ndk_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from wangqiang";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_ndk_MainActivity_callC(JNIEnv *env, jobject thiz) {
    jfieldID titleFiled = env->GetFieldID(env->GetObjectClass(thiz), "title", "Ljava/lang/String;");
    env->SetObjectField(thiz, titleFiled, env->NewStringUTF("native 层修改"));

    jmethodID method = env->GetMethodID(env->GetObjectClass(thiz), "getName", "()V");
    env->CallVoidMethod(thiz, method);
}