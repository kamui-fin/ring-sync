from flask import Flask, request
from plyer import notification

app = Flask(__name__)

@app.route("/incoming", methods=["POST"])
def log_incoming():
    content = request.get_json(silent = True)
    number = content["number"]
    notification.notify(
        title = "Incoming Call",
        message = f"You are receiving a call from {number}",
        app_icon = "phone.ico",
        timeout = 10
    )
    # play_ringtone()
    return "ok", 200

@app.route("/stop-ringtone", methods=["POST"])
def stop_ringtone():
    # stop_ringtone()
    return "ok", 200
