import React, { useState } from "react";
import "./styles.css";

export default function UrlShortener() {
  const [longUrl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [qrCodeUrl, setQrCodeUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setShortUrl("");
    setQrCodeUrl("");
    const inputEl = document.getElementById("inputEl");

    try {
      const API_URL =
        import.meta.env.REACT_APP_BACKEND_URL || "http://localhost:8080";
      const response = await fetch(`${API_URL}/api/shorten`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ longUrl }),
      });

      if (!response.ok) {
        throw new Error("Failed to shorten URL. Please try again.");
      }
      const data = await response.json();
      setShortUrl(data.shortUrl);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
      inputEl.value = "";
    }
  };

  const handleGenerateQR = () => {
    if (!shortUrl) {
      setError("Please shorten a URL first");
    }

    const shortCode = shortUrl.split("/").pop();
    setQrCodeUrl(`http://localhost:8080/api/qrcode/${shortCode}`);
  };

  return (
    <>
      <div className="main-div">
        <div className="main-rect">
          <h1>URL Shortener</h1>
          <form onSubmit={handleSubmit}>
            <input
              className="input-field"
              id="inputEl"
              type="url"
              placeholder="Enter the URL"
              value={longUrl}
              onChange={(e) => setLongUrl(e.target.value)}
              required
            />
            <br />
            <button type="submit" style={{ margin: "20px 0" }}>
              {loading ? "Shortening...." : "Shorten URL"}
            </button>
          </form>
          {shortUrl && (
            <div>
              <p>Short URL:</p>
              <a
                href={shortUrl}
                target="_blank"
                rel="noopener noreferrer"
                style={{
                  display: "inline-block",
                  marginBottom: "10px",
                  color: "#2563eb",
                }}
              >
                {shortUrl}
              </a>
              <br />
              <div className="util-buttons">
                <button
                    onClick={() => navigator.clipboard.writeText(shortUrl)}
                    style={{ padding: "0.5rem 1rem", marginLeft: "1rem" }}
                >
                    Copy URL
                </button>
                <button
                    onClick={handleGenerateQR}
                    style={{ padding: "0.5rem 1rem", marginLeft: "1rem" }}
                >
                    Generate QR Code
                </button>
              </div>
            </div>
          )}
          {qrCodeUrl && (
            <div style={{ marginTop: "1rem" }}>
              <p>QR Code:</p>
              <img
                src={qrCodeUrl}
                alt="QR Code"
                style={{ border: "1px solid #ccc" }}
              />
            </div>
          )}
          {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
      </div>
    </>
  );
}
