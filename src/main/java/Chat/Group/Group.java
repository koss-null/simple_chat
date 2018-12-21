package Chat.Group;

import Chat.User.User;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Group implements AutoCloseable {
    public static final String GroupType = "group";
    private static final List<Group> allGroups = new ArrayList<>();
    private static transient Semaphore semaphore = new Semaphore(1);


    public String name;
    private List<User> members = new ArrayList<>();
    private UUID id;

    public Group(final String name, final User creator) {
        class SameMemberExists extends Exception {private SameMemberExists(){super();}}
        boolean acquired = false;

        try {
            semaphore.acquire();
            acquired = true;
            var exist = allGroups.stream().parallel().anyMatch(g -> g.name.equals(name));
            if (exist) {
                throw new SameMemberExists();
            }

            this.name = name;
            this.id = UUID.randomUUID();
            this.members.add(creator);
            allGroups.add(this);
        } catch (InterruptedException e) {
            // todo: add logger here
            System.out.println("Failed to create a group, try one more time");
        } catch (SameMemberExists e) {
            // todo: add logger here
            System.out.println("This name is already reserved for an another group");
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }
    }

    @Override
    public void close() {
        Remove();
    }

    private void Remove() {
        boolean acquired = false;

        try {
            semaphore.acquire();
            acquired = true;
            allGroups.removeIf(g -> g.id.equals(this.id));
        } catch (InterruptedException e) {
            // todo: add logger here
            System.out.println("Failed to create a group, try one more time");
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }
    }

    public String[] getKey() {
        return new String[]{GroupType, this.id.toString()};
    }

    private Boolean checkAccess(final User user) {
        if (members == null) {
            return false;
        }

        final UUID uid = user.id;
        Predicate<User> pIsUser = u -> u.id.equals(uid);
        return members.stream().parallel().anyMatch(pIsUser);
    }

    private Map<UUID, Boolean> checkAccess(final User... user) {
        Map<UUID, Boolean> access = new HashMap<>();

        if (members == null) {
            for (User u : user) {
                access.put(u.id, false);
            }
            return access;
        }

        final BiFunction<User[], List<User>, List<Boolean>> equalsOne = (User[] toCheck, List<User> users) ->
            users.stream().map(
                    u -> Stream.of(toCheck).parallel().anyMatch(x -> x.id == u.id)
            ).collect(Collectors.toList());

        List<Boolean> equality = equalsOne.apply(user, this.members);
        int i = 0;
        for (UUID uid : List.of(user).stream().map(x -> x.id).collect(Collectors.toList())) {
            access.put(uid, equality.get(i++));
        }

        return access;
    }

    public boolean AddMember(User initiator, User toAdd) {
        boolean acquired = false;
        try {
            semaphore.acquire();
            acquired = true;

            var access = checkAccess(initiator, toAdd);
            if (!access.get(initiator.id)) {
                // have no access rights
                return false;
            }
            if (access.get(toAdd.id)) {
                // already is a member
                return true;
            }

            this.members.add(toAdd);
            return true;
        } catch (InterruptedException e) {
            // todo: add logger here
            System.out.println("Failed to add user, try again");
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }

        return false;
    }
}
