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



import data.Resource;
import gestalt.G;
import gestalt.extension.framebufferobject.JoglFrameBufferObject;
import gestalt.extension.framebufferobject.JoglTexCreatorFBO_DepthRGBA;
import gestalt.material.TexturePlugin;
import gestalt.material.texture.Bitmap;
import gestalt.material.texture.Bitmaps;
import gestalt.material.texture.bitmap.ByteBitmap;
import gestalt.model.Model;
import gestalt.render.AnimatorRenderer;
import gestalt.render.controller.FrameSetup;
import gestalt.shape.Plane;
import processing.core.*;

import java.io.InputStream;

import gestalt.extension.glsl.ShaderManager;
import gestalt.extension.glsl.ShaderProgram;
import gestalt.context.DisplayCapabilities;
import gestalt.context.GLContext;
import gestalt.model.ModelData;
import gestalt.model.ModelLoaderOBJ;
import gestalt.render.controller.FrameSetup;
import gestalt.material.Material;
import gestalt.shape.Mesh;
import gestalt.material.MaterialPlugin;

public class CreatureDrawer {

	PApplet p;
	G g;
	private DNA dna;
	private PVector loc;
	private PVector vel;
	private float h = 100;

	private float thet = 0;
	
	//Model myModel;
	AnimatorRenderer r;
	
	public CreatureDrawer(PApplet _p, DNA _dna, PVector _loc, PVector _vel) {
		p = _p;
		dna = _dna;
		loc = _loc;
		vel = _vel;

		float s = 150;
		float b = 255;
		// hue:
		float h1 = dna.getGene(40) * 255;
		float h2 = dna.getGene(41) * 255;
		float h3 = dna.getGene(42) * 255;
		float h4 = dna.getGene(43) * 360;

		int siz = 25;

	}
	
	private Model _myObject;
	private Model _myGlowObject;
    private JoglGLSLGaussianBlur _myBlur;
	
	public CreatureDrawer(PApplet _p,AnimatorRenderer _r, G _g, DNA _dna, PVector _loc, PVector _vel) {
		p = _p;
		r = _r;
		g = _g;
		dna = _dna;
		loc = _loc;
		vel = _vel;

		float s = 150;
		float b = 255;
		// hue:
		float h1 = dna.getGene(40) * 255;
		float h2 = dna.getGene(41) * 255;
		float h3 = dna.getGene(42) * 255;
		float h4 = dna.getGene(43) * 360;

		int siz = 25;
		
		JoglFrameBufferObject myFBO = createFBO();
		r.bin(g.BIN_3D_FINISH).add(myFBO);
		/*
		Plane myGlowPlane = r.drawablefactory().plane();
        myGlowPlane.material().addPlugin(myFBO);
        myGlowPlane.material().depthtest = false;
        myGlowPlane.material().blendmode = g.MATERIAL_BLEND_INVERS_MULTIPLY;
        myGlowPlane.scale().set(r.displaycapabilities().width, r.displaycapabilities().height);
        //r.bin(g.BIN_2D_FOREGROUND).add(myGlowPlane);
       */
        _myObject = createModel("resource/bixo/bixo3.obj",siz);
        _myObject.mesh().material().blendmode = g.BITMAP_BLENDMODE_ADD;
        //_myObject.mesh().material().color4f().set(80 / 255f, 170 / 255f, 255 / 255f);

        TexturePlugin myTexture = r.drawablefactory().texture();
        myTexture.load(Bitmaps.getBitmap(Resource.getStream("resource/bixo/bixomask.png")));
       
        _myObject.mesh().material().addPlugin(myTexture);
        float c1 = p.random(0,1);
        float c2 = p.random(0,1);
        float c3 = p.random(0,1);
        _myObject.mesh().material().color4f().set(c1,c2,c3, 1f);
       
        float s1 = p.random(10,30);
        float s2 = p.random(20,30);
        _myObject.mesh().scale(s1,s2);
        r.bin(g.BIN_3D_FINISH).add(_myObject);
        /*
        _myGlowObject = createModel("resource/bixo/bixo3.obj",siz+10);
        //TODO: herdar cor aqui>
        _myGlowObject.mesh().material().color4f().set(0.1f,0.2f,1f, 0.7f);
        _myGlowObject.mesh().setRotationRef(_myObject.mesh().rotation());
        myFBO.add(_myGlowObject);
        
        ShaderManager myShaderManager = r.drawablefactory().extensions().shadermanager();
        ShaderProgram myShaderProgram = myShaderManager.createShaderProgram();
        r.bin(g.BIN_FRAME_SETUP).add(myShaderManager);
        /*
        _myBlur = new JoglGLSLGaussianBlur(myShaderManager,
                myShaderProgram,
                Resource.getStream("resource/data/demo/shader/simple.vsh"),
                Resource.getStream("resource/data/demo/shader/blur.fsh"),
                myGlowPlane.material().texture().getPixelHeight());
        
        _myBlur.blursize = 20f;
        _myBlur.blurspread = 1f;

        myGlowPlane.material().addPlugin(_myBlur);
        */
        /*
		myModel = g.model(
				Resource.getStream("resource/bixo/bixo2.obj"),
				Resource.getStream("resource/bixo/planaria.png"),
				true);
		//myModel.mesh().material().replaceTexture(myImageTexture);
		//myModel.mesh().material().lit = false;
		myModel.mesh().material().depthtest =false;
		//myModel.mesh().material().smoothshading = true;
		//myModel.mesh().material().blendmode = g.MATERIAL_NORMAL_RESCALE_NORMALS;
		myModel.mesh().scale(10, 10, 10);
		r.bin(g.BIN_3D_FINISH).add(myModel);
		*/
	}
	
	// TODO: criar métodos de renderização OPENGL.
	public void glRender(PVector l, PVector v) {
		loc = l;
		vel = v;
		
		float x = loc.x;
		float y = loc.y;
		
		
		float theta = vel.heading2D();
		
		_myObject.mesh().position(x, y,3f);
		_myObject.mesh().rotation(0, 0, theta);
		/*
		_myGlowObject.mesh().position(x, y,3f);
		_myGlowObject.mesh().rotation(0, 0, theta);
		*/
		
	}

	// sem shape
	public void render(PVector l, PVector v) {
		// fill(h, s, b);
		// espinha dorçal:
		loc = l;
		vel = v;
		float s = 150;
		float b = 255;
		// hue:
		float h1 = dna.getGene(40) * 360;
		float h2 = dna.getGene(41) * 360;
		float h3 = dna.getGene(42) * 360;
		float h4 = dna.getGene(43) * 360;

		float theta = vel.heading2D();

		p.pushStyle();
		p.pushMatrix();
		p.stroke(h1, s, b + 100, 255);
		p.translate(loc.x, loc.y);
		p.rotate(theta);
		p.fill(h1, s, b, 255);
		p.ellipse(0, 0, 15, 5); // <>//
		p.stroke(h2, s, b, 200);
		p.noFill();
		p.ellipse(0, 0, 30, 30);
		p.popMatrix();
		p.popStyle();

	}
	
    private JoglFrameBufferObject createFBO() {
        JoglFrameBufferObject myFBO = new JoglFrameBufferObject(r.displaycapabilities().width,
                                                                r.displaycapabilities().height,
                                                                r.drawablefactory().camera(),
                                                                new JoglTexCreatorFBO_DepthRGBA());
        /* set backgroundcolor of FBO */
        myFBO.backgroundcolor().set(0, 1);

        /* connect camera to the FBO */
        myFBO.setCameraRef(g.camera());

        /* create a framesetup for the FBO the clears the screen */
        FrameSetup myFrameSetup = r.drawablefactory().frameSetup();
        myFrameSetup.colorbufferclearing = true;
        myFrameSetup.depthbufferclearing = true;
        myFBO.add(myFrameSetup);

        return myFBO;
    }
    
    private Model createModel(String source, float scale) {
        Model myModel;
        ModelData myModelData = ModelLoaderOBJ.getModelData(Resource.getStream(source));
        Mesh myModelMesh = r.drawablefactory().mesh(true,
                                                  myModelData.vertices, 3,
                                                  myModelData.vertexColors, 4,
                                                  myModelData.texCoordinates, 2,
                                                  myModelData.normals,
                                                  myModelData.primitive);

        myModel = r.drawablefactory().model(myModelData, myModelMesh);
        myModelMesh.material().transparent = true;
        myModelMesh.material().depthtest = false;
        myModel.mesh().scale(scale, scale, scale);
        return myModel;
    }
    
    private class JoglGLSLGaussianBlur
    implements MaterialPlugin {

    private static final int KERNEL_SIZE = 16;

    private final ShaderManager _myMaterialShaderManager;

    private final ShaderProgram _myMaterialShaderProgram;

    private final float[] _myWeight = new float[KERNEL_SIZE];

    private final float[] _myOffset = new float[KERNEL_SIZE];

    public float blursize = 10;

    public float blurspread = 1;

    private final float _myTextureSize;

    public JoglGLSLGaussianBlur(final ShaderManager theMaterialShaderManager,
                                final ShaderProgram theMaterialShaderProgram,
                                final InputStream theVertexShaderCode,
                                final InputStream theFragmentShaderCode,
                                final int theTextureSize) {
        _myMaterialShaderManager = theMaterialShaderManager;
        _myMaterialShaderProgram = theMaterialShaderProgram;

        _myMaterialShaderManager.attachVertexShader(_myMaterialShaderProgram, theVertexShaderCode);
        _myMaterialShaderManager.attachFragmentShader(_myMaterialShaderProgram, theFragmentShaderCode);

        _myTextureSize = 1.0f / theTextureSize;
    }


    public void begin(GLContext theRenderContext, Material theParent) {
        /* enable shader */
        _myMaterialShaderManager.enable(_myMaterialShaderProgram);

        getGaussianOffsets(_myOffset,
                           _myWeight,
                           blursize);

        _myMaterialShaderManager.setUniform(_myMaterialShaderProgram, "weight", _myWeight);
        _myMaterialShaderManager.setUniform(_myMaterialShaderProgram, "offset", _myOffset);
        _myMaterialShaderManager.setUniform(_myMaterialShaderProgram, "spread", blurspread);
    }


    public void end(final GLContext theRenderContext, final Material theParent) {
        _myMaterialShaderManager.disable();
    }


    private float gaussianDistribution(float theValue, float theRadius) {
        theRadius *= theRadius;
        double myValue = 1 / Math.sqrt(2.0 * Math.PI * theRadius);
        return (float) (myValue * Math.exp( (theValue * theValue) / ( -2.0 * theRadius)));
    }


    private void getGaussianOffsets(float[] theOffset,
                                    float[] theWeight,
                                    float theBlurSize) {
        theWeight[0] = 1.0f * gaussianDistribution(0.0f, theBlurSize);
        theOffset[0] = 0.0f;
        theOffset[1] = 0.0f;

        for (int i = 1; i < KERNEL_SIZE - 1; i += 2) {
            theOffset[i] = (float) i * _myTextureSize;
            theOffset[i + 1] = (float) ( -i) * _myTextureSize;
            theWeight[i] = 2.0f * gaussianDistribution(i, theBlurSize);
            theWeight[i + 1] = 2.0f * gaussianDistribution(i + 1.0f, theBlurSize);
        }
    }
}
	

}
