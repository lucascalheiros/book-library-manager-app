package com.github.lucascalheiros.booklibrarymanager.databinding;
import com.github.lucascalheiros.booklibrarymanager.R;
import com.github.lucascalheiros.booklibrarymanager.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentHomeBindingImpl extends FragmentHomeBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.uploadButton, 2);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final androidx.recyclerview.widget.RecyclerView mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentHomeBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private FragmentHomeBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[2]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (androidx.recyclerview.widget.RecyclerView) bindings[1];
        this.mboundView1.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        if (BR.viewModel == variableId) {
            setViewModel((com.github.lucascalheiros.booklibrarymanager.ui.home.HomeViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.github.lucascalheiros.booklibrarymanager.ui.home.HomeViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModelFileItemListener((androidx.lifecycle.MutableLiveData<com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener>) object, fieldId);
            case 1 :
                return onChangeViewModelFileItems((androidx.lifecycle.MutableLiveData<java.util.List<com.github.lucascalheiros.booklibrarymanager.model.BookLibFile>>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelFileItemListener(androidx.lifecycle.MutableLiveData<com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener> ViewModelFileItemListener, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelFileItems(androidx.lifecycle.MutableLiveData<java.util.List<com.github.lucascalheiros.booklibrarymanager.model.BookLibFile>> ViewModelFileItems, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
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
        androidx.lifecycle.MutableLiveData<com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener> viewModelFileItemListener = null;
        androidx.lifecycle.MutableLiveData<java.util.List<com.github.lucascalheiros.booklibrarymanager.model.BookLibFile>> viewModelFileItems = null;
        java.util.List<com.github.lucascalheiros.booklibrarymanager.model.BookLibFile> viewModelFileItemsGetValue = null;
        com.github.lucascalheiros.booklibrarymanager.ui.home.HomeViewModel viewModel = mViewModel;
        com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener viewModelFileItemListenerGetValue = null;

        if ((dirtyFlags & 0xfL) != 0) {



                if (viewModel != null) {
                    // read viewModel.fileItemListener
                    viewModelFileItemListener = viewModel.getFileItemListener();
                    // read viewModel.fileItems
                    viewModelFileItems = viewModel.getFileItems();
                }
                updateLiveDataRegistration(0, viewModelFileItemListener);
                updateLiveDataRegistration(1, viewModelFileItems);


                if (viewModelFileItemListener != null) {
                    // read viewModel.fileItemListener.getValue()
                    viewModelFileItemListenerGetValue = viewModelFileItemListener.getValue();
                }
                if (viewModelFileItems != null) {
                    // read viewModel.fileItems.getValue()
                    viewModelFileItemsGetValue = viewModelFileItems.getValue();
                }
        }
        // batch finished
        if ((dirtyFlags & 0xfL) != 0) {
            // api target 1

            com.github.lucascalheiros.booklibrarymanager.ui.home.adapters.FileListAdapter.bind(this.mboundView1, viewModelFileItemsGetValue, viewModelFileItemListenerGetValue);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.fileItemListener
        flag 1 (0x2L): viewModel.fileItems
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}