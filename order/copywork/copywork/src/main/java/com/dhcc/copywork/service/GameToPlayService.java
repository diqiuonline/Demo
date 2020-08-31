package com.dhcc.copywork.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhcc.copywork.convert.GameConvert;
import com.dhcc.copywork.convert.PlayCateConvert;
import com.dhcc.copywork.convert.PlayConvert;
import com.dhcc.copywork.entity.*;
import com.dhcc.copywork.entity.response.CommonCode;
import com.dhcc.copywork.entity.response.ResponseResult;
import com.dhcc.copywork.exception.ExceptionCast;
import com.dhcc.copywork.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class GameToPlayService {
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private PlayMapper playMapper;
    @Autowired
    private PlayCateMapper playCateMapper;
    @Autowired
    private GameTimeMapper gameTimeMapper;
    @Autowired
    private GameTemplateMapper gameTemplateMapper;
    @Autowired
    private PlayTemplateMapper playTemplateMapper;
    @Autowired
    private PlayCateTemplateMapper playCateTemplateMapper;
    @Autowired
    private GameTimeTemplateMapper gameTimeTemplateMapper;
    @Autowired
    private GameConfigMapper gameConfigMapper;


    public ResponseResult gameToPlay(int oldGameId, int newGameID, int fre, String gameName, String gameCode,  int start, int stop)  {
        //游戏期数
        try {
            int turn = 1440 / fre;
            //期数的长度
            int num = String.valueOf(turn).length();
            //要复制的游戏id
            //int oldGameId = 28;
            //新的游戏id
            //int newGameID = 328;
            //几分钟一注
            //int fre = 3;
            //游戏名字
            //String gameName = "三分快乐十分";
            //游戏code
            //String gameCode = "3fklsf";
            //supercode 去除几位
            //int del = 3;
            //延迟几秒开始
        /*int start = 1;
        //提前几秒结束
        int stop = 3;*/
            //复制game和play
            GameTemplate gameTemplate = gameTemplateMapper.selectOne(new LambdaQueryWrapper<GameTemplate>().eq(GameTemplate::getId, oldGameId));
            gameTemplate.setId(newGameID);
            gameTemplate.setName(gameName);
            gameTemplate.setCode(gameCode);
            gameTemplate.setInterval(fre);
            gameTemplate.setAmount(turn);
            gameTemplate.setTurnLength(num);
            gameMapper.insert(GameConvert.INSTANCE.templateToEntiey(gameTemplate));
            //int iee = 1/0;
            List<PlayTemplate> playTemplates = playTemplateMapper.selectList(new LambdaQueryWrapper<PlayTemplate>().eq(PlayTemplate::getGameId, oldGameId));

            for (PlayTemplate playTemplate : playTemplates) {
                playTemplate.setId(null);
                playTemplate.setGameId(newGameID);
                playTemplate.setCode(newGameID + playTemplate.getCodeBase());
                playMapper.insert(PlayConvert.INSTANCE.templateToEntiey(playTemplate));
            }
            //复制playcat

            List<PlayCateTemplate> playCateTemplates = playCateTemplateMapper.selectList(new LambdaQueryWrapper<PlayCateTemplate>().eq(PlayCateTemplate::getGameId, oldGameId));
            for (PlayCateTemplate playCateTemplate : playCateTemplates) {
                String code = null;
                if ("9".equals(playCateTemplate.getCode().substring(0, 1))) {
                    code = "9" + newGameID + playCateTemplate.getCodeBase();
                } else {
                    code = newGameID + playCateTemplate.getCodeBase();
                }
                playCateTemplate.setGameId(newGameID);
                playCateTemplate.setCode(code);
                playCateTemplate.setSuperCode(Long.valueOf(code.substring(0, code.length() - 3)));
                playCateTemplate.setPaths(playCateTemplate.getPaths().replace(String.valueOf(oldGameId), String.valueOf(newGameID)));
                playCateMapper.insert(PlayCateConvert.INSTANCE.templateToEntiey(playCateTemplate));
            }
            //生成gametime
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date starttime = sdf.parse("00:00:00");
            //int ie = 1 /0;
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar.setTime(starttime);
            calendar2.setTime(starttime);

            for (int i = 1; i <= turn; i++) {
                GameTime gameTime = new GameTime();
                gameTime.setGameId(newGameID);
                gameTime.setCode(gameCode);
                gameTime.setCrossDay(0);
                if (num == 4) {
                    if (i < 10) {
                        gameTime.setTurnNum("000" + i);
                    } else if (i >= 10 && i < 100) {
                        gameTime.setTurnNum("00" + i);
                    } else if (i >= 100 && i < 1000) {
                        gameTime.setTurnNum("0" + i);
                    } else {
                        gameTime.setTurnNum("" + i);
                    }
                } else if (num == 3) {
                    if (i < 10) {
                        gameTime.setTurnNum("00" + i);
                    } else if (i >= 10 && i < 100) {
                        gameTime.setTurnNum("0" + i);
                    } else if (i >= 100 && i < 1000) {
                        gameTime.setTurnNum("" + i);
                    }
                } else if (num == 2) {
                    if (i < 10) {
                        gameTime.setTurnNum("0" + i);
                    } else if (i >= 10 && i < 100) {
                        gameTime.setTurnNum("" + i);
                    }
                } else if (num == 1) {
                    gameTime.setTurnNum("" + i);
                }

                calendar.add(Calendar.SECOND, start);
                Date time = calendar.getTime();
                String starttime1 = sdf.format(time);
                gameTime.setStartTime(starttime1);

                calendar.add(Calendar.MINUTE, fre);
                calendar.add(Calendar.SECOND, -start - stop);
                Date time2 = calendar.getTime();
                String endTime1 = sdf.format(time2);
                gameTime.setEndTime(endTime1);

                gameTimeMapper.insert(gameTime);
                calendar2.add(Calendar.MINUTE, fre);
                Date time1 = calendar2.getTime();
                calendar.setTime(time1);
            }

            return new ResponseResult(CommonCode.SUCCESS, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CommonCode.FAIL,e.getCause().getMessage());
            return null;
        }
    }

    public ResponseResult copyGameAndPlayAndPlayCateAndGameTime(){
        try {
            gameMapper.truncateGameAndPlayAndPlayCateAndGameTime();
            List<GameConfig> gameConfigs = gameConfigMapper.selectList(null);
            for (GameConfig gameConfig : gameConfigs) {
                if (gameConfig.getId().equals(gameConfig.getTemplateGameId())) {
                    gameMapper.copyGameAndPlayAndPlayCateAndGameTime(gameConfig.getId());
                } else {
                    int fre = gameConfig.getJsIntervalSeconds() / 60;
                    //int fre = 1440/i;
                    gameToPlay(gameConfig.getTemplateGameId(), gameConfig.getId(), fre, gameConfig.getName(), gameConfig.getCode(),
                            gameConfig.getTimeStartDelay(), gameConfig.getTimeEndEarly());
                }
            }
            return new ResponseResult(CommonCode.SUCCESS, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CommonCode.FAIL,e.getCause().getMessage());
            return null;
        }
    }
}
