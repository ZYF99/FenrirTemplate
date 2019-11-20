package com.fenrir.fenrirtemplate.ui.login

import android.graphics.Paint
import com.fenrir.fenrirtemplate.R
import com.fenrir.fenrirtemplate.databinding.FragmentLoginBinding
import com.fenrir.fenrirtemplate.model.sharedpref.SharedPrefModel
import com.fenrir.fenrirtemplate.ui.base.BaseFragment


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    LoginViewModel::class.java, layoutRes = R.layout.fragment_login
) {


    override fun initView() {
        binding.viewModel = viewModel

        binding.loginButton.setOnClickListener {
            viewModel.login()
        }

        binding.forgetPasswordText.paintFlags =
            Paint.UNDERLINE_TEXT_FLAG.or(binding.forgetPasswordText.paintFlags)

        viewModel.loginSuccess.observeNonNull {
            if (it) {

            }
        }

    }


    override fun initData() {
        viewModel.email.value = SharedPrefModel.email
    }


}