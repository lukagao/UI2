#include <jni.h>
#include <malloc.h>
#include <stdio.h>
#include <stdlib.h>

JNIEXPORT jint JNICALL Java_com_test_ui2_handlers_memFillHandler_fillMem
        (JNIEnv * env, jobject obj, jint blockNum) {
    return fill((int)blockNum);
}

JNIEXPORT jint JNICALL Java_com_test_ui2_handlers_memFillHandler_freeMem
        (JNIEnv * env, jobject obj) {
    return freeMem();
}

char *p;
const int BASE_SIZE = 1024*1024; // 1M

int fill(int blockNum)
{
    int memSize = blockNum * BASE_SIZE;
    p = (char *)malloc(memSize);
    if(p==NULL){
        return 0;
    }
    int i;
    for (i = 0; i < memSize; i++)
    {
        p[i] = 0;
    }

    return blockNum;
}

int freeMem()
{
    free(p);
    return 0;
}