import React, { useState } from "react";
import './styles.css'

export default function UrlShortener() {
    const [longUrl, setLongUrl] = useState("");
    const [shortUrl, setShortUrl] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async(e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setShortUrl("")
        const inputEl = document.getElementById("inputEl");

        try {
            const API_URL = import.meta.env.REACT_APP_BACKEND_URL || "http://localhost:8080"
            const response = await fetch(`${API_URL}/api/shorten`, {
                method: "POST",
                headers: {
                    "Content-Type" : "application/json"
                },
                body: JSON.stringify({longUrl})
            });

            if(!response.ok) {
                throw new Error("Failed to shorten URL. Please try again.");
            }
            const data = await response.json();
            setShortUrl(data.shortUrl);
        } catch(err) {
            setError(err.message)
        } finally {
            setLoading(false);
            inputEl.value = '';
        }
    };

    return (
        <>
            <div className="main-div">
                <h1>URL Shortener</h1>
                <form onSubmit={handleSubmit}>
                    <input id="inputEl" type="url" placeholder="Enter the URL" value={longUrl} onChange={(e) => setLongUrl(e.target.value)} required/>
                    <br />
                    <button type="submit">
                        {loading ? "Shortening...." : "Shorten URL"}
                    </button>
                </form>
                {shortUrl && (
                    <div>
                        <p>Short URL: </p>
                        <a href={shortUrl} target="_blank" rel="noopener noreferrer">{shortUrl}</a>
                    </div>
                )}
                {error && <p style={{color: "red"}}>{error}</p>}
            </div>
        </>
    )
}