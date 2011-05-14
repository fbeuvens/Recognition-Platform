/* $Id: Jwintab.c,v 1.2 2000/02/01 05:40:21 rekimoto Exp $ */

#include <jni.h>

#include "jwintab_Jwintab.h"
#include "mywintab.h"

/*
 * Class:     jwintab_Jwintab
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_jwintab_Jwintab_close
  (JNIEnv *env, jclass jc) {
    return (jint)mwClose();
}

/*
 * Class:     jwintab_Jwintab
 * Method:    getPacket
 * Signature: ([I)I
 */
#define NUM_VAL 6
JNIEXPORT jint JNICALL Java_jwintab_Jwintab_getPacket
  (JNIEnv *env, jclass jc, jintArray ar) {
	int vals[NUM_VAL];

	int res = mwGetPacket(vals); /* poll a packet */

	if (res > 0) {
		jsize len = (*env)->GetArrayLength(env, ar);
		jint *body = (*env)->GetIntArrayElements(env, ar, 0);
		
		if (len >= NUM_VAL && body != NULL) {
			int i;
			for (i = 0; i < NUM_VAL; i++)
				body[i] = vals[i];
		} else {
			res = -1; /* bad parameter */
		}		
        (*env)->ReleaseIntArrayElements(env, ar, body, 0);
    } 
	return (jint)res;
}

/*
 * Class:     jwintab_Jwintab
 * Method:    getVersion
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_jwintab_Jwintab_getVersion
  (JNIEnv *env, jclass jc) {
	/* return (jint)20000108; */
	return (jint)20000131;
}

/*
 * Class:     jwintab_Jwintab
 * Method:    open
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_jwintab_Jwintab_open
  (JNIEnv *env, jclass jc) {
	return (jint)mwOpen();
}


