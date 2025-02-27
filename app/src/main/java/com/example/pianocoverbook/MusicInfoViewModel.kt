package com.example.pianocoverbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicInfoViewModel(
    private val repository: MusicInfoRepository,
    private val musicInfoDao: MusicInfoDao
) : ViewModel() {

    private val _musicInfo = MutableStateFlow<List<MusicInfo>>(emptyList())
    val musicInfo: StateFlow<List<MusicInfo>> = _musicInfo

    init {
        viewModelScope.launch {
            _musicInfo.value = musicInfoDao.getAllMusicInfo()
        }
    }

    fun saveValues(textOfMusic: String, textOfArtist: String, textOfMemo: String) {
        viewModelScope.launch {
            repository.saveMusicInfo(textOfMusic, textOfArtist, textOfMemo)
        }
    }

    fun insertMusicInfo(musicInfo: MusicInfo) {
        viewModelScope.launch {
            musicInfoDao.insertMusicInfo(musicInfo)
            _musicInfo.value = musicInfoDao.getAllMusicInfo()
        }
    }

    fun deleteMusicInfo(musicInfo: MusicInfo) {
        viewModelScope.launch {
            musicInfoDao.deleteMusicInfo(musicInfo)
            _musicInfo.value = musicInfoDao.getAllMusicInfo()
        }
    }
}