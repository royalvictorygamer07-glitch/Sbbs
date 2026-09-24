package com.example.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.*

object SyntaxHighlighter {

    private val commonKeywords = setOf(
        "fun", "val", "var", "class", "interface", "object", "return", "if", "else", "when",
        "for", "while", "do", "import", "package", "override", "public", "private", "protected",
        "def", "import", "from", "as", "class", "return", "if", "elif", "else", "for", "while",
        "async", "await", "try", "except", "finally", "with", "yield", "lambda", "const", "let",
        "function", "export", "default", "switch", "case", "break", "continue", "throw", "new",
        "typeof", "instanceof", "struct", "impl", "fn", "pub", "mut", "use", "mod", "trait",
        "select", "insert", "update", "delete", "create", "table", "where", "from", "join"
    )

    private val commonTypes = setOf(
        "String", "Int", "Long", "Float", "Double", "Boolean", "List", "Map", "Set", "Unit",
        "Any", "Flow", "StateFlow", "int", "float", "double", "char", "bool", "void", "vector",
        "string", "str", "dict", "tuple", "i32", "i64", "u32", "u64", "f32", "f64", "usize",
        "Self", "Promise", "Record", "Option", "Result"
    )

    fun highlight(code: String, language: String): AnnotatedString {
        return buildAnnotatedString {
            append(code)

            // Highlight line comments: // ... or # ...
            val lineCommentRegex = Regex("""(//.*|#.*)""")
            for (match in lineCommentRegex.findAll(code)) {
                addStyle(
                    style = SpanStyle(color = SyntaxComment, fontWeight = FontWeight.Normal),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }

            // Highlight block comments: /* ... */
            val blockCommentRegex = Regex("""(/\*[\s\S]*?\*/)""")
            for (match in blockCommentRegex.findAll(code)) {
                addStyle(
                    style = SpanStyle(color = SyntaxComment, fontWeight = FontWeight.Normal),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }

            // Highlight Strings: "..." or '...' or `...`
            val stringRegex = Regex(""""([^"\\]|\\.)*"|'([^'\\]|\\.)*'|`([^`\\]|\\.)*`""")
            for (match in stringRegex.findAll(code)) {
                addStyle(
                    style = SpanStyle(color = SyntaxString),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }

            // Highlight Numbers: integers and floats
            val numberRegex = Regex("""\b\d+(\.\d+)?\b""")
            for (match in numberRegex.findAll(code)) {
                addStyle(
                    style = SpanStyle(color = SyntaxNumber),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }

            // Highlight Annotations: @Composable, @dataclass, etc.
            val annotationRegex = Regex("""@[A-Za-z0-9_]+""")
            for (match in annotationRegex.findAll(code)) {
                addStyle(
                    style = SpanStyle(color = SyntaxAnnotation, fontWeight = FontWeight.SemiBold),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }

            // Highlight Words (Keywords & Types)
            val wordRegex = Regex("""\b[A-Za-z_][A-Za-z0-9_]*\b""")
            for (match in wordRegex.findAll(code)) {
                val word = match.value
                val lower = word.lowercase()

                if (commonKeywords.contains(lower)) {
                    addStyle(
                        style = SpanStyle(color = SyntaxKeyword, fontWeight = FontWeight.Bold),
                        start = match.range.first,
                        end = match.range.last + 1
                    )
                } else if (commonTypes.contains(word) || (word.firstOrNull()?.isUpperCase() == true && word.length > 1)) {
                    addStyle(
                        style = SpanStyle(color = SyntaxType, fontWeight = FontWeight.Medium),
                        start = match.range.first,
                        end = match.range.last + 1
                    )
                }
            }

            // Highlight Function invocations: funcName(...)
            val funcRegex = Regex("""\b([A-Za-z_][A-Za-z0-9_]*)\s*(?=\()""")
            for (match in funcRegex.findAll(code)) {
                val range = match.groups[1]?.range ?: continue
                val name = match.groups[1]?.value.orEmpty()
                if (!commonKeywords.contains(name.lowercase())) {
                    addStyle(
                        style = SpanStyle(color = SyntaxFunction),
                        start = range.first,
                        end = range.last + 1
                    )
                }
            }
        }
    }
}
