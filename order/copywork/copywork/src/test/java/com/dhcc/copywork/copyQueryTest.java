package com.dhcc.copywork;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhcc.copywork.entity.Game;
import com.dhcc.copywork.entity.GameTime;
import com.dhcc.copywork.entity.PlayCate;
import com.dhcc.copywork.entity.Play;
import com.dhcc.copywork.mapper.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class copyQueryTest {
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private PlayMapper playMapper;
    @Autowired
    private PlayCateMapper playCateMapper;
    @Autowired
    private GameTimeMapper gameTimeMapper;




    @Test
    public void gameToPlay() {
        //要复制的游戏id
        int oldGameId = 28;
        //新的游戏id
        int newGameID = 328;
        //几分钟一注
        int fre = 3;
        //游戏名字
        String gameName = "三分快乐十分";
        //游戏类型
        String gameType = "gdklsf";
        //游戏code
        String gameCode = "3fklsf";

        Game game = gameMapper.selectOne(new LambdaQueryWrapper<Game>().eq(Game::getId, oldGameId));
        game.setId(newGameID);
        game.setName(gameName);
        game.setType(gameType);
        game.setCode(gameCode);
        game.setInterval(fre);
        int num = String.valueOf(1440 / fre).length();
        game.setTurnLength(num);
        gameMapper.insert(game);
        List<Play> plays = playMapper.selectList(new LambdaQueryWrapper<Play>().eq(Play::getGameId, oldGameId));
        for (Play play : plays) {
            play.setId(null);
            play.setGameId(newGameID);
            play.setCode(newGameID + play.getCodeBase());
            playMapper.insert(play);
        }
    }
    @Test
    public void gameToPlayCat() {
        //要复制的游戏id
        int oldGameId = 28;
        //新的游戏id
        int newGameID = 328;
        //supercode 去除几位
        int del = 3;

        List<PlayCate> playCates = playCateMapper.selectList(new LambdaQueryWrapper<PlayCate>().eq(PlayCate::getGameId, oldGameId));
        for (PlayCate playCate : playCates) {
            String code = null;
            if ("9".equals(playCate.getCode().substring(0, 1))) {
                code   = "9" + newGameID + playCate.getCodeBase();
            } else {
                code = newGameID + playCate.getCodeBase();
            }
            playCate.setGameId(newGameID);
            playCate.setCode(code);
            playCate.setSuperCode(Long.valueOf(code.substring(0, code.length() - del)));
            playCate.setPaths(playCate.getPaths().replace(String.valueOf(oldGameId), String.valueOf(newGameID)));
            playCateMapper.insert(playCate);
        }
    }
    @Test
    public void gameToGameTime() throws Exception {
        //几分钟一注
        int fre = 3;
        //延迟几秒开始
        int start = 1;
        //提前几秒结束
        int stop = 3;
        //游戏主键
        int  gameId = 328;
        //游戏code
        String code = "3fklsf";


        int turn = 1440 / fre;
        int num = String.valueOf(turn).length();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date starttime = sdf.parse("00:00:00");

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(starttime);
        calendar2.setTime(starttime);

        for (int i = 1; i <= turn; i++) {
            GameTime gameTime = new GameTime();
            gameTime.setGameId(gameId);
            gameTime.setCode(code);
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


    }

    @Test
    public void truncateTable() {
        gameMapper.truncateGameAndPlayAndPlayCateAndGameTime();
    }
    @Test
    public void copyTable() {
        gameMapper.copyGameAndPlayAndPlayCateAndGameTime(28);
    }
}
