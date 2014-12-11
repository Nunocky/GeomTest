package org.nunocky;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends ApplicationAdapter {
    public static final String TAG="MyGdxGame";
    public static final float W = 1024;
    public static final float H = 480;
	SpriteBatch batch;
	Texture bg, bgtile, img;

    OrthographicCamera camera;
    Viewport viewport;
    Stage stage;
    ShapeRenderer shapeRenderer;

    //private Vector2 worldBottomLeft, worldTopRight;
    private Vector2 worldBottomLeft = new Vector2();
    private Vector2 worldTopRight = new Vector2();

    private float worldWidth, worldHeight;

    Group group0, group1, group2;

    @Override
	public void create () {
        calcWorldSize();

        camera = new OrthographicCamera(worldWidth, worldHeight);
//        camera.setToOrtho(false, W, H);
        camera.setToOrtho(false, worldWidth, worldHeight);
        Vector3 p0 = camera.position;
        camera.position.set(p0.x - (worldWidth - W) / 2, p0.y - (worldHeight - H) / 2, 0);
        camera.update();
//        viewport = new ScreenViewport(camera);
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        shapeRenderer = new ShapeRenderer();

        img = new Texture("badlogic.jpg");
        bg = new Texture(Gdx.files.internal("bg.jpg"));
        bgtile = new Texture("bgtile.png");

        {
            group1 = new Group();
            group1.addActor(new Actor(){
                @Override
                public void draw(Batch batch, float parentAlpha) {
                    batch.draw(bg, 0, 0);
                    batch.draw(img, 0, 0);
                }
            });
            stage.addActor(group1);
        }
    }

    void calcWorldSize() {

        final float screenRatio = ((float)Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();
        final float worldRatio  = W / H;

        if (screenRatio > worldRatio) {
            worldBottomLeft.y = 0;
            worldTopRight.y = H;

            float w = screenRatio * H;
            worldBottomLeft.x =   - (w - W)/2;
            worldTopRight.x   = W + (w - W)/2;
        }
        else {
            worldBottomLeft.x = 0;
            worldTopRight.x = W;

            float h = W / screenRatio;
            worldBottomLeft.y =   - (h - H)/2;
            worldTopRight.y   = H + (h - H)/2;
        }

        Gdx.app.log(TAG, "worldBottomLeft : " + worldBottomLeft.toString());
        Gdx.app.log(TAG, "worldTopRight : " + worldTopRight.toString());

        worldWidth = worldTopRight.x - worldBottomLeft.x;
        worldHeight = worldTopRight.y - worldBottomLeft.y;

    }

    @Override
    public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}

    @Override
    public void resize(int width, int height) {
        //viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();

    }
}
