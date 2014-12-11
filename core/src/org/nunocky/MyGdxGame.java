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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends ApplicationAdapter {
    public static final String TAG="MyGdxGame";
    public static final float W = 900;
    public static final float H = 480;
	SpriteBatch batch;
	Texture bg, bgtile, img;
    int tilesW, tilesH;

    OrthographicCamera camera;
    Viewport viewport;
    Stage stage;
    ShapeRenderer shapeRenderer;

    private Vector2 worldBottomLeft = new Vector2();
    private Vector2 worldTopRight = new Vector2();
    private float worldWidth, worldHeight, worldScale;

    Group group0, group1, group2; // 背景、ゲーム画面、コンソール

    @Override
	public void create () {
        calcWorldSize();

        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        shapeRenderer = new ShapeRenderer();

        img = new Texture("badlogic.jpg");
        bg = new Texture(Gdx.files.internal("bg.jpg"));
        bgtile = new Texture("bgtile.png");
        bgtile.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        tilesW = (int)worldWidth / bgtile.getWidth() + 1;
        tilesH = (int)worldHeight / bgtile.getHeight() + 1;

        {
            group0 = new Group();
            group0.addActor(new Actor(){
                @Override
                public void draw(Batch batch, float parentAlpha) {
                    batch.draw(bgtile,
                            worldBottomLeft.x, worldBottomLeft.y,
                            bgtile.getWidth() * tilesW,
                            bgtile.getHeight() * tilesH,
                            0, tilesH, tilesW, 0);
                }
            });
            stage.addActor(group0);
        }
        {
            group1 = new Group();
            group1.addActor(new Actor(){
                @Override
                public void draw(Batch batch, float parentAlpha) {
                    batch.draw(bg, 0, 0, W, H);
                    batch.draw(img, (W-img.getWidth())/2, (H - img.getHeight())/2);
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

            worldScale = ((float)Gdx.graphics.getHeight()) / worldHeight;
        }
        else {
            worldBottomLeft.x = 0;
            worldTopRight.x = W;

            float h = W / screenRatio;
            worldBottomLeft.y =   - (h - H)/2;
            worldTopRight.y   = H + (h - H)/2;

            worldScale = ((float)Gdx.graphics.getWidth()) / W;
        }

        worldWidth = worldTopRight.x - worldBottomLeft.x;
        worldHeight = worldTopRight.y - worldBottomLeft.y;

        Gdx.app.log(TAG, "worldBottomLeft : " + worldBottomLeft.toString());
        Gdx.app.log(TAG, "worldTopRight : " + worldTopRight.toString());
        Gdx.app.log(TAG, String.format("screen : %d x %d (%f)", Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), screenRatio));
        Gdx.app.log(TAG, String.format("world  : %.0f x %.0f (%f)", worldWidth, worldHeight, worldWidth/worldHeight));

        camera = new OrthographicCamera(worldWidth, worldHeight);
        camera.setToOrtho(false, worldWidth, worldHeight);
//        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        camera.update();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
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
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}
