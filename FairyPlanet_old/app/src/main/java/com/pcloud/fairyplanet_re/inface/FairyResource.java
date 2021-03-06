package com.pcloud.fairyplanet_re.inface;

import android.opengl.GLES10;
import android.opengl.GLUtils;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.pcloud.fairyplanet_re.core.view.GLView;
import com.pcloud.fairyplanet_re.domain.FairyInfo;
import com.pcloud.fairyplanet_re.domain.UnitInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_FRONT_AND_BACK;

/**
 * Created by PCloud on 2018-06-15.
 */

public class FairyResource extends UnitResource {
    private Map<UnitAction, ArrayList<Integer>> actionImageHandles;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            windowManager.updateViewLayout(glView, layoutParams);
        }
    };

    public FairyResource(GLView glView, Handler handler, WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
        super(FairyInfo.getInstance(), glView, handler, windowManager, layoutParams);
        this.actionImageHandles = new HashMap<>();
    }

    @Override
    public void loadResource() throws Exception {
        resourceLoader.loadingResource(unitInfo, actionImageHandles);
    }

    private boolean isMove(UnitAction unitAction) {
        if (UnitAction.move == unitAction) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(GL10 gl10) {//0.08f
        gl10.glScalef(FairyInfo.getInstance().getVerticalFocus(), 1.0f, -0.0f);
        if (fpsManager.isElapsed(1.08f)) {
            int size = actionImageHandles.get(FairyInfo.getInstance().getUnitAction()).size();
            int min = actionImageHandles.get(FairyInfo.getInstance().getUnitAction()).get(0);
            int max = actionImageHandles.get(FairyInfo.getInstance().getUnitAction()).get(size - 1) - 1;

            if (focus < min || focus > max) {
                focus = min;
            } else {
                focus++;
            }
        }

//        gl10.glEnable(GL10.GL_TEXTURE_2D);
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, focus);
        gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, floatBuffer);//텍스쳐 좌표설졍
        gl10.glDrawElements(GL10.GL_TRIANGLES, index.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);//그리기 부분.
        //TODO 메시지를 너무 많이 보내지않는건가 생각됨 프레임 처리를 해야할 거라 생각된다.
//        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
//        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

//        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl10.glFrontFace(GL10.GL_CCW);
//        gl10.glVertexPointer(3,GL10.GL_FLOAT,0,floatBuffer);//텍스쳐 좌표설졍
//        gl10.glTexCoordPointer(2,GL10.GL_FLOAT,0,textureBuffer);
//        gl10.glDrawElements(GL10.GL_TRIANGLES,index.length,GL10.GL_UNSIGNED_BYTE,indexBuffer);//그리기 부분.
//        //10gl.glDisable(GL10.GL_DEPTH_TEST);
//        //10gl.glDisable(GL10.GL_VERTEX_ARRAY);//텍스쳐
//        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    @Override
    public void move(GL10 gl10) {
        if (isMove(FairyInfo.getInstance().getUnitAction())) {
            if (FairyInfo.getInstance().getVerticalFocus() == 1 && FairyInfo.getInstance().getWindowPointX() <= FairyInfo.getInstance().getWindowMaxX()) {
                layoutParams.x = FairyInfo.getInstance().getWindowPointX() + 1;
            } else if (FairyInfo.getInstance().getVerticalFocus() == -1 && FairyInfo.getInstance().getWindowPointX() <= FairyInfo.getInstance().getWindowPointX()) {
                layoutParams.x = FairyInfo.getInstance().getWindowPointX() - 1;
            } else {
                Log.d("TSET", "발동!");
                FairyInfo.getInstance().setVerticalFocus();
                gl10.glScalef(FairyInfo.getInstance().getVerticalFocus(), 1.0f, -0.0f);
            }

            if (FairyInfo.getInstance().getHorizontalFocus() == 1 && FairyInfo.getInstance().getWindowPointY() <= FairyInfo.getInstance().getWindowMaxY()) {
                layoutParams.y = FairyInfo.getInstance().getWindowPointY() + 1;
            } else if (FairyInfo.getInstance().getHorizontalFocus() == -1 && FairyInfo.getInstance().getWindowPointY() <= FairyInfo.getInstance().getWindowMaxY()) {
                layoutParams.y = FairyInfo.getInstance().getWindowPointY() - 1;
            }

            FairyInfo.getInstance().setWindowPointX(layoutParams.x);
            FairyInfo.getInstance().setWindowPointY(layoutParams.y);
            Log.d("TEST", "vertical:" + FairyInfo.getInstance().getVerticalFocus());
            Log.d("TEST", "X:" + FairyInfo.getInstance().getWindowPointX());
            handler.post(runnable);
        }
    }
}
