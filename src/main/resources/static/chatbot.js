// MediBot AI Chatbot - Shared Logic
// Works across PatientHome.html, DoctorHome.html, AdminHome.html, Welcome.html

(function() {
  const BACKEND_URL = 'http://localhost:8080';

  // ── Inject chatbot popup HTML ──────────────────────────────────────────────
  const chatbotHTML = `
<div id="chatbot-overlay" style="display:none;position:fixed;z-index:9999;left:0;top:0;width:100%;height:100%;background:rgba(0,0,0,0.5);justify-content:center;align-items:center;">
  <div id="chatbot-box" style="background:#fff;border-radius:16px;width:90%;max-width:480px;height:580px;display:flex;flex-direction:column;box-shadow:0 8px 32px rgba(0,0,0,0.25);overflow:hidden;position:relative;">
    <!-- Header -->
    <div style="background:#006666;color:#fff;padding:16px 20px;display:flex;justify-content:space-between;align-items:center;">
      <div style="display:flex;align-items:center;gap:10px;">
        <div style="width:36px;height:36px;background:#00b3b3;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:18px;">🤖</div>
        <div>
          <div style="font-weight:700;font-size:1rem;">MediBot</div>
          <div style="font-size:0.75rem;color:#b2dfdb;">AI Health Assistant</div>
        </div>
      </div>
      <span id="chatbot-close" style="cursor:pointer;font-size:1.6rem;line-height:1;color:#b2dfdb;">&times;</span>
    </div>
    <!-- Messages -->
    <div id="chatbot-messages" style="flex:1;overflow-y:auto;padding:16px;display:flex;flex-direction:column;gap:10px;background:#f4f9f9;"></div>
    <!-- Typing indicator -->
    <div id="chatbot-typing" style="display:none;padding:0 16px 6px;color:#666;font-size:13px;font-style:italic;">MediBot is typing...</div>
    <!-- Input -->
    <div style="padding:12px 16px;border-top:1px solid #e0e0e0;background:#fff;display:flex;gap:8px;">
      <input id="chatbot-input" type="text" placeholder="Ask about symptoms, medications, health tips..."
        style="flex:1;padding:10px 14px;border:1px solid #ccc;border-radius:50px;font-size:0.9rem;outline:none;font-family:inherit;"
        autocomplete="off" />
      <button id="chatbot-send" style="background:#006666;color:#fff;border:none;border-radius:50%;width:42px;height:42px;cursor:pointer;font-size:18px;display:flex;align-items:center;justify-content:center;flex-shrink:0;">&#9658;</button>
    </div>
  </div>
</div>`;

  document.body.insertAdjacentHTML('beforeend', chatbotHTML);

  // ── Session chat history ───────────────────────────────────────────────────
  let chatHistory = [];

  // ── DOM refs ───────────────────────────────────────────────────────────────
  const overlay    = document.getElementById('chatbot-overlay');
  const messagesEl = document.getElementById('chatbot-messages');
  const inputEl    = document.getElementById('chatbot-input');
  const sendBtn    = document.getElementById('chatbot-send');
  const typingEl   = document.getElementById('chatbot-typing');
  const closeBtn   = document.getElementById('chatbot-close');

  // ── Open / close ───────────────────────────────────────────────────────────
  function openChatbot() {
    overlay.style.display = 'flex';
    if (chatHistory.length === 0) {
      addMessage('bot', "👋 Hi! I'm **MediBot**, your AI health assistant.\n\nI can help with:\n• Symptom guidance\n• Health tips\n• Doctor specialization info\n• General medical questions\n\nHow can I help you today?");
    }
    setTimeout(() => inputEl.focus(), 100);
  }

  function closeChatbot() {
    overlay.style.display = 'none';
  }

  closeBtn.addEventListener('click', closeChatbot);
  overlay.addEventListener('click', e => { if (e.target === overlay) closeChatbot(); });

  // ── Add message bubble ─────────────────────────────────────────────────────
  function addMessage(role, text) {
    const isBot = role === 'bot';
    const bubble = document.createElement('div');
    bubble.style.cssText = `display:flex;justify-content:${isBot ? 'flex-start' : 'flex-end'};`;

    const inner = document.createElement('div');
    inner.style.cssText = `
      max-width:80%;padding:10px 14px;border-radius:${isBot ? '4px 16px 16px 16px' : '16px 4px 16px 16px'};
      background:${isBot ? '#fff' : '#006666'};color:${isBot ? '#333' : '#fff'};
      font-size:0.88rem;line-height:1.5;box-shadow:0 1px 4px rgba(0,0,0,0.12);
      white-space:pre-wrap;word-break:break-word;`;
    inner.innerHTML = text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>').replace(/\n/g, '<br>');

    bubble.appendChild(inner);
    messagesEl.appendChild(bubble);
    scrollToBottom();

    chatHistory.push({ role: isBot ? 'assistant' : 'user', text });
  }

  function scrollToBottom() {
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  // ── Typing effect ──────────────────────────────────────────────────────────
  function typeMessage(text) {
    const bubble = document.createElement('div');
    bubble.style.cssText = 'display:flex;justify-content:flex-start;';

    const inner = document.createElement('div');
    inner.style.cssText = `
      max-width:80%;padding:10px 14px;border-radius:4px 16px 16px 16px;
      background:#fff;color:#333;font-size:0.88rem;line-height:1.5;
      box-shadow:0 1px 4px rgba(0,0,0,0.12);white-space:pre-wrap;word-break:break-word;`;

    bubble.appendChild(inner);
    messagesEl.appendChild(bubble);

    let i = 0;
    const htmlText = text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    const rawText  = text.replace(/\*\*(.*?)\*\*/g, '$1');

    function typeChar() {
      if (i < rawText.length) {
        inner.textContent = rawText.substring(0, i + 1);
        i++;
        scrollToBottom();
        setTimeout(typeChar, 12);
      } else {
        inner.innerHTML = htmlText.replace(/\n/g, '<br>');
        scrollToBottom();
        chatHistory.push({ role: 'assistant', text });
      }
    }
    typeChar();
  }

  // ── Send message ───────────────────────────────────────────────────────────
  async function sendMessage() {
    const text = inputEl.value.trim();
    if (!text) return;

    addMessage('user', text);
    inputEl.value = '';
    sendBtn.disabled = true;
    inputEl.disabled = true;
    typingEl.style.display = 'block';

    try {
      const res = await fetch(`${BACKEND_URL}/api/chatbot/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: text })
      });

      const data = await res.json();
      typingEl.style.display = 'none';

      if (res.ok && data.reply) {
        typeMessage(data.reply);
      } else {
        typeMessage('Sorry, I encountered an error. Please try again. ' + (data.error || ''));
      }
    } catch (err) {
      typingEl.style.display = 'none';
      typeMessage('⚠️ Cannot connect to the chatbot service. Please make sure the backend is running on port 8080.');
    } finally {
      sendBtn.disabled = false;
      inputEl.disabled = false;
      inputEl.focus();
    }
  }

  sendBtn.addEventListener('click', sendMessage);
  inputEl.addEventListener('keydown', e => { if (e.key === 'Enter') sendMessage(); });

  // ── Wire up existing chatbot cards / nav links ─────────────────────────────
  function wireUpChatbotTriggers() {
    document.querySelectorAll('.card').forEach(card => {
      const img = card.querySelector('img');
      const h2  = card.querySelector('h2');
      if ((img && img.src && img.src.includes('ChatbotImage')) ||
          (h2  && (h2.textContent.includes('Chatbot') || h2.textContent.includes('Staff Support')))) {
        card.style.cursor = 'pointer';
        card.addEventListener('click', openChatbot);
      }
    });

    document.querySelectorAll('a[href="#chatbot"]').forEach(a => {
      a.addEventListener('click', e => { e.preventDefault(); openChatbot(); });
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', wireUpChatbotTriggers);
  } else {
    wireUpChatbotTriggers();
  }

  // Expose globally so pages can call openMediBot() directly
  window.openMediBot = openChatbot;
})();
