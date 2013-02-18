//Biophandria
//
//Copyright (C) 2013 Marcos Frankowicz + Jack de Castro Holmer
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package biophandria;

//TEST FOR CHANGES.

import gestalt.G;
import gestalt.context.Display;
import gestalt.context.DisplayCapabilities;
import gestalt.demo.basic.UsingDisplay;
import gestalt.extension.framebufferobject.JoglFrameBufferObject;
import gestalt.extension.glsl.ShaderManager;
import gestalt.extension.gpgpu.cawater.GPGPUCAWater;
import gestalt.material.Color;
import gestalt.material.TexturePlugin;
import gestalt.material.texture.Bitmaps;
import gestalt.model.Model;
import gestalt.render.AnimatorRenderer;
import gestalt.render.Drawable;
import gestalt.render.SketchRenderer;
import gestalt.render.bin.DisposableBin;
import gestalt.shape.Plane;
import processing.core.PApplet;
import processing.core.PVector;

import data.Resource;

import java.util.ArrayList;
import java.util.Vector;

import mathematik.Vector2f;
import mathematik.Vector3f;

public class MainGestMode extends SketchRenderer {
	
	static G g;
	DisposableBin gd = new DisposableBin();
	PApplet p;
	Biome bio;
	Plane _myPlane;
	
	private Plane _myWaterView;
	
	private GPGPUCAWater _mySimulation;
	
	private JoglFrameBufferObject _myInputEnergyMap;
	
	private ArrayList<PVector> locs = new ArrayList<PVector>();

	public void setup() {
			
		//new MainGestMode().init();
		
		p = new PApplet();
		bio = new Biome(p,this,g);

		camera().setMode(CAMERA_MODE_ROTATE_XYZ);
		//createPlane();
		TexturePlugin myTexture = drawablefactory().texture();
		myTexture.load(Bitmaps.getBitmap(Resource.getStream("resource/bixo/top.png")));
		Plane top = G.plane();
		top.material().addPlugin(myTexture);
		top.scale(Global.worldSize*2, Global.worldSize*2);
		top.position(0, 0, 5);
		bin(BIN_3D_FINISH).add(top);
		
		gd.addTextureToPlanePrimitve(Bitmaps.getBitmap(Resource.getStream("resource/bixo/bixomask.png")));
		bin(BIN_3D_FINISH).add(gd);
		
		/* setup shader */
        final ShaderManager myShaderManager = drawablefactory().extensions().shadermanager();
        bin(BIN_FRAME_SETUP).add(myShaderManager);
        
        /* heightmap */
        _myInputEnergyMap = JoglFrameBufferObject.createRectangular(Global.worldSize*2, Global.worldSize*2);
        _myInputEnergyMap.add(gd);
        _myInputEnergyMap.scale().set(Global.worldSize*2, Global.worldSize*2);
        bin(BIN_FRAME_SETUP).add(_myInputEnergyMap);
        
        final Plane myHeightfieldView = G.plane();
        myHeightfieldView.material().addTexture(_myInputEnergyMap);
        myHeightfieldView.scale(Global.worldSize*2, Global.worldSize*2);
        myHeightfieldView.setPlaneSizeToTextureSize();
        myHeightfieldView.material().diffuse = new Color(100, 100, 255, 255);
        //bin(BIN_3D_FINISH).add(myHeightfieldView);
        //myHeightfieldView.position().x -= myHeightfieldView.scale().x * 0.5f;
        
        /* simulation */
        _mySimulation = new GPGPUCAWater(myShaderManager,
                                         _myInputEnergyMap,
                                         "resource/data/demo/shader/gpgpu/CAWaterSimulation.fs",
                                         "resource/data/demo/shader/gpgpu/CAWaterDrawer.fs");
        bin(BIN_FRAME_SETUP).add(_mySimulation);

        /* simulation view */
        _myWaterView = G.plane();
        _mySimulation.attachWater(_myWaterView.material());
        _myWaterView.setPlaneSizeToTextureSize();
        
        //_myWaterView.position().x += _myWaterView.scale().x * 0.5f + 1;
		
        for(int i = 0; i<bio.creatures.size(); i++){
        	locs.add(bio.creatures.get(i).loc);
        }
	}
	
	
	public void loop(final float theDeltaTime) {
		
		_mySimulation.damping = 0.98f;
		
		bio.run();
		bio.glRender(this);
		moveCamera(theDeltaTime);
		for(int i = 0; i<bio.creatures.size(); i++){
			Vector3f vlocs = new Vector3f();
			PVector plocs = bio.creatures.get(i).loc;
			vlocs.set(plocs.x,plocs.y,plocs.z);
			gd.plane(vlocs, 25);
			//gd.circle(vlocs, 13f);
			
		}
	}
	
	private void moveCamera(float theDeltaTime) {
        float mySpeed = 300f * theDeltaTime;
        /* move camera */
        if (event().keyCode == KEYCODE_A) {
            camera().forward(mySpeed);
        }
        if (event().keyCode == KEYCODE_Q) {
            camera().forward( -mySpeed);
        }
        if (event().keyCode == KEYCODE_LEFT) {
            camera().side( -mySpeed);
        }
        if (event().keyCode == KEYCODE_RIGHT) {
            camera().side(mySpeed);
        }
        if (event().keyCode == KEYCODE_DOWN) {
            camera().up( -mySpeed);
        }
        if (event().keyCode == KEYCODE_UP) {
            camera().up(mySpeed);
        }
        if (event().keyCode == KEYCODE_W) {
            camera().fovy += mySpeed;
        }
        if (event().keyCode == KEYCODE_S) {
            camera().fovy -= mySpeed;
        }
    }
	
	private void createPlane() {
        /* gestalt uses a drawablefactory to create shapes.
         * in a more advanced demo we will see how custom shapes can be created
         * without using the drawablefactory().
         */
        _myPlane = drawablefactory().plane();

        /* each shape has a transform. a transform is a matrix that defines
         * the shapes position (translation) and rotation in space.
         * you can also use position() to define the translation in the
         * transform matrix.
         * note that 'rotation()' and 'scale()' do not affect the
         * transform matrix.
         * also see 'AbstractShape.rotation()' and 'AbstractShape.scale()'
         */
        //_myPlane.transform().translation.z = -20;

        /* each shape also has a seperate scale */
        _myPlane.scale().x = Global.worldSize*2;
        _myPlane.scale().y = Global.worldSize*2;

        /* a shape can have different origins
         *    SHAPE_ORIGIN_BOTTOM_LEFT
         *    SHAPE_ORIGIN_BOTTOM_RIGHT
         *    SHAPE_ORIGIN_TOP_LEFT
         *    SHAPE_ORIGIN_TOP_RIGHT
         *    SHAPE_ORIGIN_CENTERED
         */
        _myPlane.origin(SHAPE_ORIGIN_CENTERED);

        /* a plane has a material. the material manages things like the shapes
         * color4f, the transparency, the blendmode and the texture.
         */
        _myPlane.material().color4f().set(0f, 0f, 0f, 1f);
        _myPlane.material().transparent = true;
        _myPlane.material().colormasking = true;

        /* finally add the shape to the renderer. */
        bin(BIN_3D).add(_myPlane);
    }
	
	public DisplayCapabilities createDisplayCapabilities() {
        /*
         * create a 'displaycapabilities' object
         */
        DisplayCapabilities myDisplayCapabilities = new DisplayCapabilities();

        /*
         * this property appears in the bar above the window and is of course
         * not visible if the window is undecorated.
         */
        myDisplayCapabilities.name = "MainGestMode";

        /* width and height define the window size in pixel. */
        myDisplayCapabilities.width = 1360;
        myDisplayCapabilities.height = 768;

        /* undecorated removes the title bar if set to true */
        myDisplayCapabilities.undecorated = true;

        /* switch to fullscreenmode */
        myDisplayCapabilities.fullscreen = false;

        /* 'centered' positions the window in the middle of the screen */
        myDisplayCapabilities.centered = true;

        /*
         * backgroundcolor specifies the background color of the window
         * ( including the alphavalue which only seems to work under osx
         * but nevertheless produces very interesting results ) and the opengl
         * clear color.
         * values range from 0 to 1, where 0 is dark or transparent and 1 is
         * light or opaque.
         */
        myDisplayCapabilities.backgroundcolor.set(1);

        /*
         * antialiasing is experimental. if 'antialiasing' is set higher than 0
         * a fullscene antialiasing is attempted, but not garantueed.
         * again, be warned.
         */
        myDisplayCapabilities.antialiasinglevel = 0;

        /* the cursor flag remove the cursor. */
        myDisplayCapabilities.cursor = false;

        /*
         * there is also a 'headless' option which prevents the renderer from
         * creating an actual window. this is used in cases where a window
         * is created by another part of an application.
         * this option is rather advanced and usually left at 'false'
         */
        myDisplayCapabilities.headless = false;

        /*
         * the 'switchresolution' flag whether the resolution of the display is
         * switched when entering fullscreen mode.
         */
        myDisplayCapabilities.switchresolution = true;

        /**
         * here can select a screen in a multi-screen enviroment.
         * the good news is it that it can help you ( on some systems ) to create
         * a window that is bigger than the default screen.
         * the bad news is it doesn t work for fullscreen mode ( at least not
         * on my mac / ati system ).
         */
        DisplayCapabilities.listDisplayDevices();
        myDisplayCapabilities.device = 0;

        return myDisplayCapabilities;
    }
	
	public static void main(String[] args) {
		//new MainGestMode().init();
		g.init(MainGestMode.class);
	}

}
