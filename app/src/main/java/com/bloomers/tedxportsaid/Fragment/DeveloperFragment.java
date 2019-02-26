package com.bloomers.tedxportsaid.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.delay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
public class DeveloperFragment extends Fragment {

    @BindView(R.id.developer_image)  ImageView developer_image;
    private Unbinder unbinder;

    public static DeveloperFragment newInstance() {
        return new DeveloperFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_developer, container, false);
        unbinder =  ButterKnife.bind(this,root);

        if (getActivity()!=null){
            GlideApp.with(getActivity()).load(R.drawable.official).circleCrop().into(developer_image);
        }

        return root;
    }


    @OnClick({R.id.developer_image,R.id.face_book,R.id.linked_in,R.id.whatsapp,R.id.gmail,R.id.google_play})
    void OnClik(View view){
        developer_image.animate().scaleX(0.95F).withEndAction(new Runnable() {
            @Override
            public void run() {
                developer_image.animate().scaleX(1).scaleY(1).start();
            }
        }).scaleY(0.95F).start();

      switch (view.getId()){
          case R.id.face_book:
          case R.id.developer_image:
              AppController.getInstance().launchFacebook(getActivity(),"profile","100002486856017");
              break;
          case R.id.linked_in:
              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/mahmoud-elfeel/")));
              break;
          case R.id.whatsapp:
              String smsNumber = "2001096579083"; //without '+'
              try {
                  Intent sendIntent = new Intent("android.intent.action.MAIN");
                  sendIntent.setAction(Intent.ACTION_SEND);
                  sendIntent.setType("text/plain");
                  sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi Mahmoud");
                  sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net");
                  sendIntent.setPackage("com.whatsapp");
                  startActivity(sendIntent);
              } catch(Exception e) {
                  openAppAfterDelay(view,R.string.whatsAppDoesntExist,"com.whatsapp");
              }
              break;
          case R.id.gmail:
              Intent i = new Intent(Intent.ACTION_SEND);
              i.setType("message/rfc822");
              i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"elfeel67@gmail.com"});
              i.putExtra(Intent.EXTRA_SUBJECT, "TEDxPortSaid");
              i.putExtra(Intent.EXTRA_TEXT   , "");
              try {
                  startActivity(Intent.createChooser(i, "Send mail..."));
              } catch (Exception ex) {
                  openAppAfterDelay(view,R.string.no_email_app,"com.google.android.gm");
              }

              break;
          case R.id.google_play:
              try {
                  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=5959613167249185073")));
              }catch (Exception e){
                  MainActivity.showCustomToast(getActivity(),getString(R.string.error_happend),null,false);
              }

              break;
      }
    }

    private void openAppAfterDelay(View view, @StringRes int string, final String packageName){
        MainActivity.showCustomToast(getActivity(),getString(string),null,false);
        new delay(view, 2000) {
            @Override
            protected void OnDelayEnded() {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                try {
                    startActivity(i);
                } catch (android.content.ActivityNotFoundException ignored) {
                    AppController.getInstance().showErrorToast(getActivity());
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
