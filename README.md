# Simple Text Chat 简单的文本聊天

一个简单的mod，通过语音转写让你可以边玩边用语音聊天。

A simple mod that allows you to chat with voices while playing by recognize your voice to chat messages.

### Intention 初衷

> “你可以一边玩一边说话，但不可以一边玩一边打字。”
> 
> —— Yuang2714在一个没有语音聊天的Minecraft服务器中的讲话
> 
> "You can speak while playing, but you can't type while playing." 
> 
> —— Yuang2714's speech on a Minecraft server that has not Simple Voice Chat installed
>
> <img width="358" height="31" alt="image" src="https://github.com/user-attachments/assets/db2dd2a3-9417-4ef8-84dc-2b786ee8685f" />

### Vosk lib Usage

本项目使用Vosk作为语音转写引擎，它使用Apache-2.0许可。

This project uses Vosk as recognization engine, which uses Apache-2.0 license.

### Recognizer Lifecycle 转写器的生命周期

```mermaid
sequenceDiagram
    participant a as Render Thread
    participant h as HUD Renderer
    participant b as Recognizer Management Thread
    participant c as Config Holder
    
    Note over h: Showing "Unloaded" Text
    activate h
    
    Note over a: User Enable Feature
    a ->> b: Start Thread
    activate b
    b ->> c: Read Config
    c -->> b: Return Config
    Note over b,c: Configs: Model Path, Mode
    Note over b: Setup Model
    Note over b: Start Recognization
    
    loop Main Loop
        Note over b: Start Recognization
        alt Hold to Speak Mode 
            a ->> b: User Pressed Button
            loop Recognization Sync
                b ->> h: Interval Particular Result
            end
            a ->> b: User Released Button
        else Release Send Mode
            loop Recognization Sync
                b ->> h: Interval Particular Result
            end
            a ->> b: User Pressed Button
        else Auto Detection Mode
            loop Recognization Sync
                b ->> h: Interval Particular Result
            end
            Note over b: When Recognizer Thinks Sentence Finished
        end
        
        b ->> h: Show Final Text
        b ->> a: Send Chat Message
    end
    
    Note over a: User Disable Feature
    a ->> b: Stop Thread
    Note over b: Close Resources
    deactivate b
    
    deactivate h
```
