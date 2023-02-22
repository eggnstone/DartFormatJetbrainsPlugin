package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

data class IndentResult(val lines: List<String>, val remainingTokens: List<IToken>)
