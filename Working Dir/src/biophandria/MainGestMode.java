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
import gestalt.render.AnimatorRenderer;
import gestalt.shape.Plane;
import processing.core.PApplet;

import data.Resource;

import java.util.Vector;

public class MainGestMode extends AnimatorRenderer {
	
	static G g;
	PApplet p;
	Biome bio;
	Plane _myPlane;
	
	public void setup() {
		p = new PApplet();
		bio = new Biome(p,this,g);
		camera().setMode(CAMERA_MODE_LOOK_AT);
		createPlane();
	}

	public void loop(final float theDeltaTime) {
		bio.run();
		bio.glRender();
		moveCamera(theDeltaTime);
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
        _myPlane.material().color4f().set(0.8f, 0.8f, 0.8f, 1f);
        _myPlane.material().transparent = true;
        _myPlane.material().colormasking = true;

        /* finally add the shape to the renderer. */
        bin(BIN_3D).add(_myPlane);
    }
	
	public static void main(String[] args) {
		//new MainGestMode().init();
		g.init(MainGestMode.class);
	}

}
