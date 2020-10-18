package app.developer.uiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;

public class SearchView extends LinearLayoutCompat {

    private AppCompatEditText searchField;
    private LinearLayoutCompat searchContainer;
    private ImageView searchImage;
    private SearchViewListener listener;

    public SearchView(Context context) {
        super(context);
        init(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_search, this, true);
        initView(view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView, 0, 0);
        searchContainer.setOnClickListener(v -> {
            searchImage.setVisibility(View.GONE);
            searchField.setVisibility(View.VISIBLE);
            searchField.requestFocus();
        });
        searchField.setOnFocusChangeListener((v, hasFocus) -> {
            searchImage.setVisibility(View.GONE);
        });
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    searchImage.setVisibility(View.GONE);
                } else {
                    hideKeyboardFrom(getContext(), searchImage);
                    searchField.clearFocus();
                    searchImage.setVisibility(View.VISIBLE);
                }
                if (listener != null) {
                    listener.onNewText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        typedArray.recycle();
    }

    private void initView(View view) {
        searchContainer = view.findViewById(R.id.search_container);
        searchImage = view.findViewById(R.id.search_image);
        searchField = view.findViewById(R.id.search_field);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setSearchViewListener(SearchViewListener listener) {
        this.listener = listener;
    }

    public interface SearchViewListener {
        void onNewText(String newText);
    }
}
