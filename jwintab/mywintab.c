/* $Id: mywintab.c,v 1.2 2000/02/01 05:40:21 rekimoto Exp $ */

#include <stdio.h>
#include <string.h>
#include <windows.h>
#include <stdlib.h>

/* wintab */
#include <wintab.h>
#ifdef USE_X_LIB
#include <wintabx.h>
#endif
#define PACKETDATA	(PK_X | PK_Y | PK_BUTTONS | PK_ORIENTATION | PK_NORMAL_PRESSURE)
#define PACKETMODE	0
#include <pktdef.h>

#include "mywintab.h"

static HCTX ctx;

static HCTX TabletInit(HWND hWnd) {
	LOGCONTEXT lcMine;

	/* get default region */
	WTInfo(WTI_DEFCONTEXT, 0, &lcMine);

	/* modify the digitizing region */
	strcpy(lcMine.lcName, "Rule Digitizing");
	lcMine.lcPktData = PACKETDATA;
	lcMine.lcPktMode = PACKETMODE;
	lcMine.lcMoveMask = 0;
	lcMine.lcBtnUpMask = lcMine.lcBtnDnMask;

	/* open the region */
	return WTOpen(hWnd, &lcMine, TRUE);
}

int mwOpen() {
	HWND hWnd;	
	
	if (ctx != NULL) return -1;
	hWnd = GetDesktopWindow();
	if (hWnd == NULL) return -1;
	ctx = TabletInit(hWnd);
	if (ctx == NULL) return -1;
	return 1;
}

int mwClose() {
	if (ctx == NULL) return 1;
	return WTClose(ctx);
}

int mwGetPacket(int val[]) {
	PACKET p;
	
	if (ctx == NULL) return -1;
	if (WTPacketsGet(ctx, 1, &p) > 0) { /* received a packet */
		val[0] = p.pkX;
		val[1] = p.pkY;
		val[2] = p.pkButtons;	
		val[3] = p.pkOrientation.orAzimuth;
		val[4] = p.pkOrientation.orAltitude;
		// added 1.29.2000
		val[5] = p.pkNormalPressure;
		return 1;
	}
	return 0; /* no packet has arrived */
}


