package ru.binaryunicorn.coloria.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class TileTapView extends View implements View.OnTouchListener
{
	private static final String KEY_COUNT_OF_HORIZONTAL_TILES = "KEY_COUNT_OF_HORIZONTAL_TILES";
	private static final String KEY_COUNT_OF_VERTICAL_TILES = "KEY_COUNT_OF_VERTICAL_TILES";
	private static final String KEY_SOUND_ENABLED = "KEY_SOUND_ENABLED";
	private static final String KEY_TILE_COLORS = "KEY_TILE_COLORS";

	private int _horizontalTilesCount;
	private int _verticalTilesCount;
	private boolean _tapSoundEnabled;

	private int[][] _tileColors;
	private int[] _sounds;

	private SoundPool _soundPool;
	private Random _randomForSound;
	private Random _randomR;
	private Random _randomG;
	private Random _randomB;
	private Paint _brush;
	private int _tileWidth;
	private int _tileHeight;
	private Point[] _tapInfos;


	public TileTapView(Context context)
	{
		super(context);
	}

	public TileTapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		_horizontalTilesCount = 0;
		_verticalTilesCount = 0;
		_tapSoundEnabled = false;

		_randomForSound = new Random();
		_randomR = new Random();
		_randomG = new Random();
		_randomB = new Random();
		_brush = new Paint();

		_tileWidth = 0;
		_tileHeight = 0;

		_tapInfos = new Point[10];
		for (int i = 0; i < 10; i++)
		{
			_tapInfos[i] = new Point(-1, -1);
		}

		setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		canvas.drawColor(Color.WHITE);

		// Создаем поле, если это необходимо
		if (_horizontalTilesCount <= 0 || _verticalTilesCount <= 0)
		{
			generateField(1, 1);
			return;
		}

		_tileWidth = (int)Math.ceil( (float)getWidth() / (float)_horizontalTilesCount );
		_tileHeight = (int)Math.ceil( (float)getHeight() / (float)_verticalTilesCount );

		// непосредственно вывод
		for (int i = 0; i < _verticalTilesCount; i++)
		{
			for (int j = 0; j < _horizontalTilesCount; j++)
			{
				_brush.setColor(_tileColors[i][j]);
				canvas.drawRect(
						_tileWidth * j,
						_tileHeight * i,
						_tileWidth * j + _tileWidth,
						_tileHeight * i + _tileHeight,
						_brush
				);
			}
		}
	}

	@Override
	protected Parcelable onSaveInstanceState()
	{
		Bundle bundle = new Bundle();

		bundle.putParcelable("superState", super.onSaveInstanceState());
		bundle.putInt(KEY_COUNT_OF_HORIZONTAL_TILES, _horizontalTilesCount);
		bundle.putInt(KEY_COUNT_OF_VERTICAL_TILES, _verticalTilesCount);
		bundle.putBoolean(KEY_SOUND_ENABLED, _tapSoundEnabled);

		int[] colors = new int[_horizontalTilesCount * _verticalTilesCount];
		for (int i = 0; i < _verticalTilesCount; i++)
		{
			System.arraycopy(_tileColors[i], 0, colors, i * _horizontalTilesCount, _horizontalTilesCount);
		}
		bundle.putIntArray(KEY_TILE_COLORS, colors);

		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		if (state instanceof Bundle)
		{
			Bundle bundle = (Bundle)state;
			state = bundle.getParcelable("superState");

			_horizontalTilesCount = bundle.getInt(KEY_COUNT_OF_HORIZONTAL_TILES);
			_verticalTilesCount = bundle.getInt(KEY_COUNT_OF_VERTICAL_TILES);
			_tapSoundEnabled = bundle.getBoolean(KEY_SOUND_ENABLED);
			int[] colors = bundle.getIntArray(KEY_TILE_COLORS);

			_tileColors = new int[_verticalTilesCount][_horizontalTilesCount];

			if (colors != null)
			{
				for (int i = 0; i < _verticalTilesCount; i++)
				{
					System.arraycopy(colors, i * _horizontalTilesCount, _tileColors[i], 0, _horizontalTilesCount);
				}
			}
			else
			{
				generateField(_horizontalTilesCount, _verticalTilesCount);
			}
		}

		super.onRestoreInstanceState(state);
	}


	//// Methods ////

	public void generateField(int horizontalTilesCount, int verticalTilesCount)
	{
		_horizontalTilesCount = horizontalTilesCount;
		_verticalTilesCount = verticalTilesCount;
		_tileColors = new int[_verticalTilesCount][_horizontalTilesCount];

		for (int i = 0; i < _verticalTilesCount; i++)
		{
			for (int j = 0; j < _horizontalTilesCount; j++)
			{
				_tileColors[i][j] = Color.rgb(_randomR.nextInt(256), _randomG.nextInt(256), _randomB.nextInt(256));
			}
		}

		invalidate();
	}

	public void setRandomColorForTile(int horizontalPos, int verticalPos)
	{
		setColorForTile(horizontalPos, verticalPos,
				Color.rgb(_randomR.nextInt(256), _randomG.nextInt(256), _randomB.nextInt(256))
		);
	}

	public void setColorForTile(int horizontalPos, int verticalPos, Integer color)
	{
		try
		{
			_tileColors[verticalPos][horizontalPos] = color;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
	}

	public void tapSoundEnabled(boolean enabled)
	{
		_tapSoundEnabled = enabled;
	}

	public void refresh()
	{
		invalidate();
	}

	public void setSoundpoolAndSounds(SoundPool soundPool, int[] sounds)
	{
		_soundPool = soundPool;
		_sounds = sounds;
	}

	public void releaseSoundpool()
	{
		if (_soundPool != null)
		{
			_soundPool.release();
			_soundPool = null;
			_sounds = null;
		}
	}


	//// View.OnTouchListener ////

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		int ID;
		int horizontalPos;
		int verticalPos;

		// отлов нажатия за вью
		if (event.getX() > getWidth() || event.getY() > getHeight())
		{
			return true;
		}

		switch (event.getActionMasked())
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_MOVE:
				for (int i = 0; i < event.getPointerCount(); i++)
				{
					ID = event.getPointerId(i);
					horizontalPos = (int)event.getX(i) / _tileWidth;
					verticalPos = (int)event.getY(i) / _tileHeight;

					if (horizontalPos != _tapInfos[ID].x || verticalPos != _tapInfos[ID].y)
					{
						_tapInfos[ID].x = horizontalPos;
						_tapInfos[ID].y = verticalPos;

						setRandomColorForTile(horizontalPos, verticalPos);

						if (_tapSoundEnabled && _soundPool != null && _sounds.length > 0)
						{
							_soundPool.play( _sounds[_randomForSound.nextInt(_sounds.length)], 1, 1, 0, 0, 1 );
						}
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				for (Point item : _tapInfos)
				{
					item.x = item.y = -1;
				}

				break;
			case MotionEvent.ACTION_POINTER_UP:
				ID = event.getPointerId(event.getActionIndex());
				_tapInfos[ID].x = _tapInfos[ID].y = -1;

				break;
		}

		refresh();
		return true;
	}
}
