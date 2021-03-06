package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 23:37:53 - 19.03.2010
 */
public class ScaleModifier extends BaseSingleValueSpanModifier {

    public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale) {
        this(pDuration, pFromScale, pToScale, null);
    }

    public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IModifierListener pModiferListener) {
        super(pDuration, pFromScale, pToScale, pModiferListener);
    }

    public ScaleModifier(final ScaleModifier pScaleModifier) {
        super(pScaleModifier);
    }

    @Override
    public ScaleModifier clone() {
        return new ScaleModifier(this);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void onSetInitialValue(final Shape pShape, final float pScale) {
        pShape.setScale(pScale);
    }

    @Override
    protected void onSetValue(final Shape pShape, final float pScale) {
        pShape.setScale(pScale);
    }
}
