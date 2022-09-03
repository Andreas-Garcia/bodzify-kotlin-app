package com.bpm.a447bpm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bpm.a447bpm.databinding.FragmentFirstBinding
import okhttp3.*
import java.io.IOException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    var apiUrl = ""

    private val client = OkHttpClient()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        apiUrl = resources.getString(R.string.bpm_api_url)
        run()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun run() {
        val request = Request.Builder()
            .url("$apiUrl/users/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread(java.lang.Runnable {
                    activity!!.findViewById<TextView>(R.id.textview_first).text = e.toString()
                })
            }
            override fun onResponse(call: Call, response: Response) {
                activity?.runOnUiThread(java.lang.Runnable {
                    activity!!.findViewById<TextView>(R.id.textview_first).text =
                        response.body()?.string() ?: "vide"
                })
            }
        })
    }
}