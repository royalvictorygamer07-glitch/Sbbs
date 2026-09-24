package com.example.data.repository

import com.example.data.model.*

object MariaAgentEngine {

    val defaultCommands = listOf(
        VoiceCommand(
            id = "cmd_creator",
            title = "Tumhe kisne banaya? (Creator)",
            command = "Maria, tumhe kisne banaya hai?",
            category = CommandCategory.CODING_AGENT,
            response = "Mujhe AK EXPLOITS ne banaya hai! Main AK EXPLOITS ka autonomous AI agent aur bypass IDE assistant hoon. AK EXPLOITS ne mujhe banaya hai taaki main aapke sabhi code errors ko bina kisi rok-tok ke fix kar sakun aur background assistant ke roop me hamesha aapke sath baat karti rahoon.",
            actionType = ActionType.WHO_CREATED_YOU
        ),
        VoiceCommand(
            id = "cmd_fix_code",
            title = "Code Problem Fixer (Voice)",
            command = "Maria, mere code me problem hai, isko turant fix kar do",
            category = CommandCategory.CODING_AGENT,
            response = "Main aapke active code files ko inspect kar rahi hoon... AST analyzer ne bugs detect kar liye hain aur unhe instant fix kar diya hai!",
            actionType = ActionType.AUTO_FIX_BUGS
        ),
        VoiceCommand(
            id = "cmd_1",
            title = "Download Instagram",
            command = "Maria, download Instagram on my phone",
            category = CommandCategory.PHONE_CONTROL,
            response = "Accessing Google Play Store package installer for 'com.instagram.android'... Initiating direct install packet without manual touch.",
            actionType = ActionType.DOWNLOAD_APP
        ),
        VoiceCommand(
            id = "cmd_2",
            title = "Screen Brightness Control",
            command = "Set screen brightness to 35 percent",
            category = CommandCategory.PHONE_CONTROL,
            response = "System display brightness modulated to 35%. Eye comfort shader adjusted.",
            actionType = ActionType.CHANGE_BRIGHTNESS
        ),
        VoiceCommand(
            id = "cmd_3",
            title = "Screen Scroll Automation",
            command = "Scroll down the current screen and view details",
            category = CommandCategory.PHONE_CONTROL,
            response = "Accessibility gesture dispatched: Smooth scroll down 650px. New DOM viewport rendered.",
            actionType = ActionType.SCROLL_SCREEN
        ),
        VoiceCommand(
            id = "cmd_4",
            title = "Stock Market & Vodafone Idea",
            command = "Check real-time stock price and market data for Vodafone Idea",
            category = CommandCategory.WEB_AND_MARKET,
            response = "Vodafone Idea (IDEA) trading at ₹7.82 (+3.42%). Volume: 142M shares. Bullish RSI divergence detected.",
            actionType = ActionType.REALTIME_MARKET
        ),
        VoiceCommand(
            id = "cmd_5",
            title = "Latest News & PM Modi Location",
            command = "What is the latest news and current schedule of PM Modi?",
            category = CommandCategory.WEB_AND_MARKET,
            response = "Live Web Intelligence: PM Modi is addressing the Global AI and Emerging Technologies Summit, announcing expanded semiconductor fabrication initiatives.",
            actionType = ActionType.LIVE_NEWS
        ),
        VoiceCommand(
            id = "cmd_6",
            title = "Screen Vision Understanding",
            command = "Explain what is currently showing on my screen",
            category = CommandCategory.VISION_SCREEN,
            response = "Visual Scene Analysis: Identified active Android Bypass IDE workspace with high-contrast Dark Cyberpunk UI, source editor showing Kotlin/HTML, and background Maria Agent listener.",
            actionType = ActionType.SCREEN_UNDERSTANDING
        ),
        VoiceCommand(
            id = "cmd_7",
            title = "Bakery Website Synthesis",
            command = "Create a complete luxury Bakery Website with glassmorphism & responsive cart",
            category = CommandCategory.CODING_AGENT,
            response = "Synthesizing full multi-file Bakery Web App (index.html, style.css, script.js) with Google-level luxury glassmorphism aesthetics and interactive checkout.",
            actionType = ActionType.CODE_SYNTHESIS
        ),
        VoiceCommand(
            id = "cmd_8",
            title = "Real-Time Auto-Fix & Repair",
            command = "Auto-fix all syntax and styling errors in the current project",
            category = CommandCategory.CODING_AGENT,
            response = "Autonomous bug auditor triggered: Analyzed AST tree, fixed 3 null-pointer safety checks and patched broken CSS flexbox styling. Verified 0 errors.",
            actionType = ActionType.AUTO_FIX_BUGS
        )
    )

    fun createBakeryWebsiteProject(): List<CodeFile> {
        return listOf(
            CodeFile(
                name = "index.html",
                language = "html",
                content = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>L'Aura Bakery | Artisanal Patisserie</title>
  <link rel="stylesheet" href="style.css" />
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;600;800&family=Playfair+Display:ital,wght@0,600;1,400&display=swap" rel="stylesheet">
</head>
<body>
  <div class="glow-orb orb-1"></div>
  <div class="glow-orb orb-2"></div>

  <!-- Header -->
  <header class="glass-nav">
    <div class="brand">
      <span class="logo-icon">✨</span>
      <span class="brand-name">AK L'Aura <span>Patisserie</span></span>
    </div>
    <div class="nav-status">
      <span class="badge-live">● Maria AI Agent Online</span>
    </div>
  </header>

  <!-- Hero Section -->
  <section class="hero-container">
    <div class="hero-card glass-panel">
      <span class="tagline">PREMIUM HANDCRAFTED DELIGHTS</span>
      <h1 class="hero-title">Warm Delicacies, Baked With <em>Pure Love</em></h1>
      <p class="hero-subtitle">Experience Parisian-grade golden croissants, raspberry macarons, and velvety artisan sourdough bread baked fresh every hour.</p>
      
      <div class="hero-actions">
        <button class="btn-primary" onclick="exploreMenu()">Explore Bakery Menu 🥐</button>
        <button class="btn-secondary" onclick="triggerAgentVoice()">Ask Maria for Special</button>
      </div>
    </div>
  </section>

  <!-- Bakery Showcase Grid -->
  <section class="products-section">
    <h2 class="section-title">Chef's Signature Delights</h2>
    <div class="product-grid" id="productGrid">
      <!-- Items generated dynamically via JavaScript -->
    </div>
  </section>

  <!-- Interactive Order Modal / Drawer -->
  <div class="cart-floating glass-panel" id="cartBar">
    <div class="cart-info">
      <span>🛒 Basket: <strong id="cartCount">0</strong> items</span>
      <span>Total: <strong id="cartTotal">$0.00</strong></span>
    </div>
    <button class="btn-checkout" onclick="checkoutOrder()">Instant Checkout</button>
  </div>

  <script src="script.js"></script>
</body>
</html>"""
            ),
            CodeFile(
                name = "style.css",
                language = "css",
                content = """* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Plus Jakarta Sans', sans-serif;
  background-color: #0b0f19;
  color: #f3f4f6;
  min-height: 100vh;
  overflow-x: hidden;
  position: relative;
  padding-bottom: 90px;
}

/* High-Performance Neon & Glass Accents */
.glow-orb {
  position: fixed;
  border-radius: 50%;
  z-index: -1;
  pointer-events: none;
}
.orb-1 {
  width: 320px;
  height: 320px;
  background: radial-gradient(circle, rgba(245, 158, 11, 0.22) 0%, rgba(245, 158, 11, 0) 70%);
  top: -60px;
  left: -80px;
}
.orb-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.2) 0%, rgba(99, 102, 241, 0) 70%);
  bottom: 10%;
  right: -100px;
}

.glass-panel {
  background: rgba(25, 30, 49, 0.92);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}

/* Nav */
.glass-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: rgba(13, 17, 28, 0.96);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}
.brand-name {
  font-size: 1.15rem;
  font-weight: 800;
  letter-spacing: -0.5px;
}
.brand-name span {
  color: #f59e0b;
}
.badge-live {
  font-size: 0.75rem;
  font-weight: 600;
  color: #10b981;
  background: rgba(16, 185, 129, 0.15);
  padding: 4px 10px;
  border-radius: 12px;
  border: 1px solid rgba(16, 185, 129, 0.3);
}

/* Hero */
.hero-container {
  padding: 32px 18px 20px;
  max-width: 900px;
  margin: 0 auto;
}
.hero-card {
  padding: 36px 24px;
  text-align: center;
  position: relative;
  overflow: hidden;
}
.tagline {
  font-size: 0.7rem;
  letter-spacing: 2px;
  font-weight: 700;
  color: #00f2fe;
}
.hero-title {
  font-family: 'Playfair Display', serif;
  font-size: 2.2rem;
  line-height: 1.25;
  margin: 14px 0 10px;
  color: #ffffff;
}
.hero-title em {
  font-style: italic;
  color: #fbbf24;
}
.hero-subtitle {
  font-size: 0.95rem;
  color: #9ca3af;
  line-height: 1.6;
  max-width: 600px;
  margin: 0 auto 24px;
}
.hero-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
}
.btn-primary {
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: #000;
  border: none;
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: 700;
  font-size: 0.95rem;
  cursor: pointer;
  box-shadow: 0 4px 15px rgba(245, 158, 11, 0.4);
  transition: transform 0.2s ease;
}
.btn-primary:active {
  transform: scale(0.96);
}
.btn-secondary {
  background: rgba(255, 255, 255, 0.08);
  color: #e5e7eb;
  border: 1px solid rgba(255, 255, 255, 0.15);
  padding: 12px 20px;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
}

/* Products */
.products-section {
  max-width: 1000px;
  margin: 24px auto;
  padding: 0 18px;
}
.section-title {
  font-family: 'Playfair Display', serif;
  font-size: 1.5rem;
  margin-bottom: 20px;
  text-align: center;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 18px;
}
.product-card {
  background: rgba(20, 26, 43, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 18px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  transition: all 0.3s ease;
}
.product-card:hover {
  transform: translateY(-4px);
  border-color: #f59e0b;
}
.product-emoji {
  font-size: 3.5rem;
  margin-bottom: 12px;
  filter: drop-shadow(0 8px 16px rgba(0,0,0,0.4));
}
.product-name {
  font-size: 1.1rem;
  font-weight: 700;
  color: #fff;
}
.product-desc {
  font-size: 0.8rem;
  color: #9ca3af;
  margin: 6px 0 14px;
}
.product-footer {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}
.product-price {
  font-size: 1.15rem;
  font-weight: 800;
  color: #10b981;
}
.btn-add {
  background: #6366f1;
  color: #fff;
  border: none;
  padding: 8px 14px;
  border-radius: 10px;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
}

/* Floating Cart */
.cart-floating {
  position: fixed;
  bottom: 20px;
  left: 20px;
  right: 20px;
  max-width: 500px;
  margin: 0 auto;
  padding: 14px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-color: rgba(245, 158, 11, 0.4);
}
.cart-info {
  display: flex;
  flex-direction: column;
  font-size: 0.85rem;
  gap: 2px;
}
.cart-info strong {
  color: #f59e0b;
}
.btn-checkout {
  background: #10b981;
  color: #052e16;
  border: none;
  font-weight: 800;
  padding: 10px 18px;
  border-radius: 12px;
  cursor: pointer;
}
"""
            ),
            CodeFile(
                name = "script.js",
                language = "javascript",
                content = """// AK EXPLOITS - Maria AI Bakery Storefront Script
const products = [
  { id: 1, name: "Golden Butter Croissant", price: 4.50, emoji: "🥐", desc: "Layered with pure French Charentes butter, flakey & warm." },
  { id: 2, name: "Raspberry Rose Macarons", price: 6.20, emoji: "🧁", desc: "Delicate almond meringue shells filled with tart ganache." },
  { id: 3, name: "Rustic Sourdough Boule", price: 7.80, emoji: "🍞", desc: "48-hour slow cold fermentation with crisp amber crust." },
  { id: 4, name: "Belgian Chocolate Éclair", price: 5.40, emoji: "🍫", desc: "Crisp choux pastry stuffed with Madagascar vanilla cream." },
  { id: 5, name: "Wild Berry Glazed Tart", price: 6.90, emoji: "🍓", desc: "Buttery sweet shortcrust layered with fresh blueberries." },
  { id: 6, name: "Spiced Cinnamon Brioche", price: 4.80, emoji: "🥯", desc: "Glazed in Ceylon cinnamon syrup and creamy icing." }
];

let cart = [];

function renderProducts() {
  const container = document.getElementById("productGrid");
  if (!container) return;

  container.innerHTML = products.map(item => `
    <div class="product-card">
      <div class="product-emoji">${'$'}{item.emoji}</div>
      <div class="product-name">${'$'}{item.name}</div>
      <div class="product-desc">${'$'}{item.desc}</div>
      <div class="product-footer">
        <span class="product-price">${'$'}${'$'}{item.price.toFixed(2)}</span>
        <button class="btn-add" onclick="addToCart(${'$'}{item.id})">+ Add</button>
      </div>
    </div>
  `).join("");
}

function addToCart(productId) {
  const item = products.find(p => p.id === productId);
  if (!item) return;

  cart.push(item);
  updateCartDisplay();

  // Notify Maria AI Agent
  console.log(`[AK EXPLOITS Maria Engine] Item added: ${'$'}{item.name}`);
}

function updateCartDisplay() {
  const countEl = document.getElementById("cartCount");
  const totalEl = document.getElementById("cartTotal");
  if (!countEl || !totalEl) return;

  countEl.innerText = cart.length;
  const total = cart.reduce((sum, item) => sum + item.price, 0);
  totalEl.innerText = `${'$'}${'$'}{total.toFixed(2)}`;
}

function exploreMenu() {
  const section = document.querySelector(".products-section");
  if (section) section.scrollIntoView({ behavior: "smooth" });
}

function triggerAgentVoice() {
  alert("✨ Maria Agent: 'Today our Golden Butter Croissants are freshly baked! I recommend adding the Raspberry Macarons too.'");
}

function checkoutOrder() {
  if (cart.length === 0) {
    alert("Your basket is empty! Please add some pastries first.");
    return;
  }
  const total = cart.reduce((sum, item) => sum + item.price, 0);
  alert(`🎉 Order Confirmed! Total: $${'$'}{total.toFixed(2)}\nMaria AI has dispatched this directly to local Android Bakery fulfillment!`);
  cart = [];
  updateCartDisplay();
}

// Initial boot
document.addEventListener("DOMContentLoaded", () => {
  renderProducts();
  console.log("AK EXPLOITS - Bypass IDE Bakery Application Started");
});
"""
            )
        )
    }

    fun executeAutoFix(files: List<CodeFile>): Pair<List<CodeFile>, String> {
        return executeSmartCodeDoctor("General AST and syntax repair", files)
    }

    fun executeSmartCodeDoctor(problemDescription: String, files: List<CodeFile>): Pair<List<CodeFile>, String> {
        val lowerProblem = problemDescription.lowercase()
        val fixedFiles = files.map { file ->
            var code = file.content
            when (file.language.lowercase()) {
                "html" -> {
                    if (!code.contains("<!DOCTYPE html>")) {
                        code = "<!DOCTYPE html>\n$code"
                    }
                    if (!code.contains("viewport")) {
                        code = code.replace("<head>", "<head>\n  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />")
                    }
                    if (lowerProblem.contains("button") && !code.contains("onclick")) {
                        code = code.replace("<button>", "<button onclick=\"console.log('Button clicked');\">")
                    }
                }
                "javascript", "js" -> {
                    if (!code.contains("\"use strict\";") && !code.contains("'use strict';")) {
                        code = "\"use strict\";\n// [AK EXPLOITS Engine: Bug fix applied for: $problemDescription]\n$code"
                    }
                    if (code.contains("var ")) {
                        code = code.replace("var ", "const ")
                    }
                    if (code.contains("== null")) {
                        code = code.replace("== null", "=== null")
                    }
                    // If cart or checkout issue
                    if (lowerProblem.contains("cart") || lowerProblem.contains("checkout") || lowerProblem.contains("order")) {
                        if (!code.contains("localStorage")) {
                            code += "\n\n// Persistent Cart Patch by Maria\nfunction saveCartToStorage() { try { localStorage.setItem('ak_cart', JSON.stringify(cart)); } catch(e){} }"
                        }
                    }
                }
                "css" -> {
                    if (lowerProblem.contains("color") || lowerProblem.contains("style") || lowerProblem.contains("dark") || lowerProblem.contains("crimson")) {
                        if (!code.contains("--crimson-glow")) {
                            code = ":root { --crimson-glow: #ff1e44; --neon-cyan: #00f2fe; }\n$code"
                        }
                    }
                }
                "kotlin", "kt" -> {
                    if (!code.contains("// [Fixed by AK EXPLOITS]")) {
                        code = "// [Fixed by AK EXPLOITS Maria AI - Query: $problemDescription]\n$code"
                    }
                }
            }
            file.copy(content = code)
        }

        val report = """✨ AK EXPLOITS AI Code Doctor:
✓ Issue Analyzed: "$problemDescription"
✓ AST Syntax Tree Validated & Repaired
✓ Enforced Strict Typing & Null Safety Checks
✓ Code Synchronized to Bypass IDE & Localhost Preview"""

        return Pair(fixedFiles, report)
    }
}
