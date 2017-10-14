package at.fhhgb.mc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;

import java.util.Random;

import at.fhhgb.mc.game.MyGame;

/**
 * This class manages the loading of
 * resources
 */
public class AssetsLoader {

    public Texture image1, image2, image3;
    public ParticleEffect[] pointEffect;

    /**
     * Loads resources such as particle effects and images
     * @param _myGame resources will be loaded in the object
     *                of MyGame
     */
    public AssetsLoader(MyGame _myGame){
        image1 = new Texture(Gdx.files.internal("blocks/blockV.png"));
        image2 = new Texture(Gdx.files.internal("blocks/block.png"));
        image3 = new Texture(Gdx.files.internal("blocks/block3.png"));

        pointEffect = new ParticleEffect[_myGame.maxPointsAmount];
        for (int i = 0; i < pointEffect.length; i++){
                pointEffect[i] = new ParticleEffect();
                if(i > 3){
                    pointEffect[i].load(Gdx.files.internal("ParticleEffects/point" + (i%3) + ".parti"),Gdx.files.internal("ParticleEffects/"));
                }else{
                    pointEffect[i].load(Gdx.files.internal("ParticleEffects/point" + i + ".parti"),Gdx.files.internal("ParticleEffects/"));
                }
            }
    }
}
