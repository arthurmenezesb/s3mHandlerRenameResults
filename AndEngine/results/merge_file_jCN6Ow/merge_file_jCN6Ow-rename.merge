package org.anddev.andengine.entity.shape.modifier; 

import org.anddev.andengine.entity.shape.IModifierListener; 
import org.anddev.andengine.entity.shape.Shape; 

/**
 * @author Nicolas Gramlich
 * @since 23:37:53 - 19.03.2010
 */
public  class  ScaleModifier  extends BaseDoubleValueSpanModifier {
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	
	

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IModifierListener pModiferListener) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pModiferListener);
	}

	

	<<<<<<< MINE
public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, null);
=======
public ScaleModifier(float pDuration, float pFromValueA, float pToValueA, float pFromValueB, float pToValueB) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB);
>>>>>>> YOURS
	}
	

	<<<<<<< MINE
public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IModifierListener pModiferListener) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pModiferListener);
=======
public ScaleModifier(float pDuration, float pFromValueA, float pToValueA, float pFromValueB, float pToValueB, IModifierListener pModiferListener) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pModiferListener);
>>>>>>> YOURS
	}
	

	public ScaleModifier(final ScaleModifier pScaleModifier) {
		super(pScaleModifier);
	}
	

	@Override
	public ScaleModifier clone(){
		return new ScaleModifier(this);
	}
	

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	<<<<<<< MINE
@Override
	protected void onSetInitialValues(final Shape pShape, final float pScaleA, final float pScaleB) {
		pShape.setScale(pScaleA, pScaleB);
=======
protected void onSetInitialValues(final Shape pShape, float pScaleX, float pScaleY) {
		pShape.setScaleX(pScaleX);
		pShape.setScaleY(pScaleY);
>>>>>>> YOURS
	}
	

	<<<<<<< MINE
@Override
	protected void onSetValues(final Shape pShape, final float pScaleA, final float pScaleB) {
		pShape.setScale(pScaleA, pScaleB);
=======
protected void onSetValues(final Shape pShape, float pScaleX, float pScaleY) {
		pShape.setScaleX(pScaleX);
		pShape.setScaleY(pScaleY);
>>>>>>> YOURS
	}

}
