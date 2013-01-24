/*
 * Gestalt
 *
 * Copyright (C) 2012 Patrick Kochlik + Dennis Paul
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * {@link http://www.gnu.org/licenses/lgpl.html}
 *
 */


package biophandria;

import gestalt.G;
import gestalt.model.Model;
import gestalt.render.AnimatorRenderer;

import data.Resource;


public class UsingANiceModel {
	AnimatorRenderer r;
	G g;
	public UsingANiceModel(AnimatorRenderer _r, G _g){
		r = _r;
		g = _g;
	}
	
    public void setup() {
        r.cameramover(true);
        r.fpscounter(true);
        r.framerate(r.UNDEFINED);

        /* load model */
        Model myModel = g.model(Resource.getStream("resource/data/demo/common/person.obj"),
                                Resource.getStream("resource/data/demo/common/person.png"),
                                true);
        myModel.mesh().material().lit = true;

        /* camera */
        r.camera().setMode(r.CAMERA_MODE_LOOK_AT);
        r.camera().lookat().set(0, 150, 0);

        /* setup light */
        r.light().enable = true;
        r.light().setPositionRef(r.camera().position());
    }


    public void loop(final float theDeltaTime) {
    }
}
