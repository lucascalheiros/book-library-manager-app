package com.github.lucascalheiros.booklibrarymanager.databinding;
import com.github.lucascalheiros.booklibrarymanager.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemFileListBindingImpl extends ItemFileListBinding implements com.github.lucascalheiros.booklibrarymanager.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView1;
    @NonNull
    private final android.widget.TextView mboundView2;
    @NonNull
    private final android.widget.TextView mboundView3;
    @NonNull
    private final android.widget.ImageButton mboundView4;
    @NonNull
    private final android.widget.ImageButton mboundView5;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemFileListBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private ItemFileListBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (android.widget.ImageButton) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView5 = (android.widget.ImageButton) bindings[5];
        this.mboundView5.setTag(null);
        setRootTag(root);
        // listeners
        mCallback1 = new com.github.lucascalheiros.booklibrarymanager.generated.callback.OnClickListener(this, 1);
        mCallback2 = new com.github.lucascalheiros.booklibrarymanager.generated.callback.OnClickListener(this, 2);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.model == variableId) {
            setModel((com.github.lucascalheiros.booklibrarymanager.model.BookLibFile) variable);
        }
        else if (BR.listener == variableId) {
            setListener((com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setModel(@Nullable com.github.lucascalheiros.booklibrarymanager.model.BookLibFile Model) {
        this.mModel = Model;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.model);
        super.requestRebind();
    }
    public void setListener(@Nullable com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener Listener) {
        this.mListener = Listener;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.github.lucascalheiros.booklibrarymanager.model.BookLibFile model = mModel;
        java.util.List<java.lang.String> modelTags = null;
        java.lang.String stringJoinJavaLangStringModelTags = null;
        float modelReadPercent = 0f;
        java.lang.String modelName = null;
        com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener listener = mListener;
        java.lang.String stringUtilsFormatFloatToPercentModelReadPercent = null;

        if ((dirtyFlags & 0x5L) != 0) {



                if (model != null) {
                    // read model.tags
                    modelTags = model.getTags();
                    // read model.readPercent
                    modelReadPercent = model.getReadPercent();
                    // read model.name
                    modelName = model.getName();
                }


                // read String.join(", ", model.tags)
                stringJoinJavaLangStringModelTags = java.lang.String.join(", ", modelTags);
                // read StringUtils.formatFloatToPercent(model.readPercent)
                stringUtilsFormatFloatToPercentModelReadPercent = com.github.lucascalheiros.booklibrarymanager.utils.StringUtils.formatFloatToPercent(modelReadPercent);
        }
        // batch finished
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, modelName);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView2, stringJoinJavaLangStringModelTags);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView3, stringUtilsFormatFloatToPercentModelReadPercent);
        }
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.mboundView4.setOnClickListener(mCallback1);
            this.mboundView5.setOnClickListener(mCallback2);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 1: {
                // localize variables for thread safety
                // model
                com.github.lucascalheiros.booklibrarymanager.model.BookLibFile model = mModel;
                // listener != null
                boolean listenerJavaLangObjectNull = false;
                // listener
                com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener listener = mListener;



                listenerJavaLangObjectNull = (listener) != (null);
                if (listenerJavaLangObjectNull) {



                    listener.download(model);
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // model
                com.github.lucascalheiros.booklibrarymanager.model.BookLibFile model = mModel;
                // listener != null
                boolean listenerJavaLangObjectNull = false;
                // listener
                com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener listener = mListener;



                listenerJavaLangObjectNull = (listener) != (null);
                if (listenerJavaLangObjectNull) {



                    listener.read(model);
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): model
        flag 1 (0x2L): listener
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}