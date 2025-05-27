package com.maadiran.myvision.presentation.features.sandbox.compose/*
package com.maadiran.myvision.presentation.features.sandbox.compose

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.maadiran.myvision.databinding.DialogPairingBinding

class PairingDialog(private val onCodeEntered: (String) -> Unit) : DialogFragment() {

    private var _binding: DialogPairingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPairingBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
            .setTitle("Pairing Code")
            .setView(binding.root)
            .setCancelable(false)

        val dialog = builder.create()

        binding.buttonSubmitCode.setOnClickListener {
            val code = binding.editPairingCode.text.toString().trim()
            if (code.isNotEmpty()) {
                onCodeEntered(code)
                dialog.dismiss()
            } else {
                binding.editPairingCode.error = "Please enter the pairing code"
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
*/