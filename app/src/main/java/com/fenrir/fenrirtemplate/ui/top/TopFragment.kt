package com.fenrir.fenrirtemplate.ui.top

import androidx.navigation.fragment.findNavController
import com.fenrir.fenrirtemplate.R
import com.fenrir.fenrirtemplate.databinding.FragmentTopBinding
import com.fenrir.fenrirtemplate.ui.base.BaseFragment


class TopFragment : BaseFragment<FragmentTopBinding, TopViewModel>(
    TopViewModel::class.java, layoutRes = R.layout.fragment_top
) {


    override fun initView() {
        binding.loginButton.setOnClickListener { findNavController().navigate(R.id.action_TopFragment_to_loginFragment) }
    }


    override fun initData() {

    }
}